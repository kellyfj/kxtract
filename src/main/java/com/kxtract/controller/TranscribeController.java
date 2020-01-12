package com.kxtract.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kxtract.data.EpisodeRepository;
import com.kxtract.data.TranscriptionRepository;
import com.kxtract.data.dao.Episode;
import com.kxtract.data.dao.Episode.ProcessingStatus;
import com.kxtract.data.dao.Transcription;
import com.kxtract.transcription.Transcriber;

@Controller
public class TranscribeController {
	private Logger logger = LoggerFactory.getLogger(TranscribeController.class);
	private static final String TRANSCRIPTION_BUCKET_NAME = "kxtract-transcriptions";
	
	@Autowired
	private EpisodeRepository episodeRepo;
	
	@Autowired
	private TranscriptionRepository transcriptionRepo;
	
	@PostMapping(value="/transcribe")
	public ModelAndView transcribe(@RequestParam String id) {
		logger.info("Transcribing episodeId (" + id + ")");
		Integer episodeUniqueId = Integer.parseInt(id);
		Episode ep = episodeRepo.findById(episodeUniqueId.intValue());
		
		//@TODO Properly handle episode not found
		if(ep == null) {
			throw new RuntimeException("Episode with unique ID (" + id + ") not found!");
		}
		
		try {
			String jobName = Transcriber.launchTranscriptionJob(ep.getS3URL(), TRANSCRIPTION_BUCKET_NAME);
			logger.info("Started transcription job (" + jobName + ")");
			ep.setProcessingStatus(ProcessingStatus.TRANSCRIPTION_STARTED);
			Transcription t = new Transcription(ep.getId());
			t = transcriptionRepo.save(t);
			
			PollingThread pollingThread = new PollingThread(t.getId(), jobName);
			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.submit(pollingThread);		
		} catch (RuntimeException e) {
			logger.info(e.getMessage(), e);
			ep.setProcessingStatus(ProcessingStatus.ERROR);
		}
		episodeRepo.save(ep);
		
		//Send back to episode list page
		return new ModelAndView("redirect:/ui/episodes");
	}
	

	
	class PollingThread implements Runnable {
		private String jobName;
		private Transcription transcription; 

		public PollingThread(int transcriptionId, String jobName) {
			this.jobName = jobName;
		    transcription = transcriptionRepo.findById(transcriptionId);
			if (transcription == null) {
				logger.error("Transcription with Id " + transcriptionId + " not found");
			}
		}

		@Override
		public void run() {
			try {
				Episode episode = episodeRepo.findById(transcription.getEpisodeId());
				
				//WAIT for transcription to be available
				String downloadUri = Transcriber.waitForTranscriptionJobToComplete(jobName);
				if (downloadUri == null) {
					logger.warn("Updating status of episode (" + episode.getId() + ") to " + ProcessingStatus.ERROR);
					episode.setProcessingStatus(ProcessingStatus.ERROR);
					episodeRepo.save(episode);
					return;
				}
				transcription.setS3_transcription_download_location(downloadUri);
				transcriptionRepo.save(transcription);
				
				//DOWNLOAD transcription
				File downloadTarget = new File("/tmp/download-" + jobName + ".json");
				logger.info("Downloading transcription from : " + downloadUri);
				FileUtils.copyURLToFile(new URL(downloadUri), downloadTarget);
				StringBuilder sb = Transcriber.simplifyTranscription(downloadTarget.getAbsolutePath());

				transcription.setRaw_transcription(sb.toString());
				transcriptionRepo.save(transcription);
				
				//UPDATE episode status
				logger.info("Updating status of episode (" + episode.getId() + ") to "
						+ ProcessingStatus.TRANSCRIPTION_COMPLETE);
				episode.setProcessingStatus(ProcessingStatus.TRANSCRIPTION_COMPLETE);
				episodeRepo.save(episode);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		}

	}
}
