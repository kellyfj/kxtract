package com.kxtract.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kxtract.data.EpisodeRepository;
import com.kxtract.data.dao.Episode;
import com.kxtract.data.dao.Episode.ProcessingStatus;
import com.kxtract.transcription.Transcriber;

@Controller
public class TranscribeController {
	private Logger logger = LoggerFactory.getLogger(TranscribeController.class);
	private static final String TRANSCRIPTION_BUCKET_NAME = "kxtract-transcriptions";
	@Autowired
	private EpisodeRepository episodeRepo;
	
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
		} catch (RuntimeException e) {
			logger.info(e.getMessage(), e);
			ep.setProcessingStatus(ProcessingStatus.ERROR);
		}
		
		episodeRepo.save(ep);
		
		//Send back to episode list page
		return new ModelAndView("redirect:/ui/episodes");
	}
}
