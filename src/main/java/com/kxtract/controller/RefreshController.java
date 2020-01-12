package com.kxtract.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kxtract.app.PodcastDownloader;
import com.kxtract.data.EpisodeRepository;
import com.kxtract.data.PodcastRepository;
import com.kxtract.data.dao.Episode.ProcessingStatus;
import com.kxtract.data.dao.Podcast;
import com.kxtract.podengine.models.Episode;
import com.kxtract.s3.S3Uploader;
import com.kxtract.transcription.Transcriber;

@Controller
public class RefreshController {
	private Logger logger = LoggerFactory.getLogger(RefreshController.class);
	private static final String DOWNLOAD_PATH = "/tmp/downloads/audio/";
	private static final String RAW_AUDIO_BUCKET_NAME = "kxtract";
	private static final String TRANSCRIPTION_BUCKET_NAME = "kxtract-transcriptions";

	@Autowired
	private PodcastRepository podRepo;

	@Autowired
	private EpisodeRepository episodeRepo;

	@GetMapping("/clearcache")
	public String clearLocalFileCache(Model model) throws IOException {
		File cacheDirectory = new File(DOWNLOAD_PATH);
		int filesBefore = cacheDirectory.list().length; 
		FileUtils.cleanDirectory(cacheDirectory);
		int filesAfter = cacheDirectory.list().length; 
		assert filesAfter == 0;
		model.addAttribute("filesDeleted", (filesBefore - filesAfter));
		return "clearcache";
	}
	
	/**
	 * TODO: Refactor the logic in here
	 * There is also the fact that there is multiple levels of state
	 *  - RSS Feed State
	 *  - Audio File Downloaded
	 *  - Row in Episode table
	 *  - Audio in S3
	 *  - Transcription State
	 * This needs some serious design thinking to reduce the management complexity
	 *  
	 * @param model
	 * @return
	 */
	@GetMapping("/refresh")
	public String refreshLatestEpisodes(Model model) {
		List<com.kxtract.data.dao.Episode> episodesDownloaded = new ArrayList<>();
		List<com.kxtract.data.dao.Episode> episodesUploadedToS3 = new ArrayList<>();
		List<String> stackTraces = new ArrayList<>();
		List<Podcast> podcastList = (List<Podcast>) podRepo.findAll();
		int podcastCount = 0;
		for (Podcast p : podcastList) {

			try {
				Episode ep = PodcastDownloader.downloadLatestEpisode(p.getRssURL(), DOWNLOAD_PATH, false);

				// IF episode downloaded THEN
				if (ep != null) {

					// Confirm it already isn't in the database
					if (episodeRepo.findByPodcastIdAndEpisodeName(p.getId(), ep.getTitle()) == null) {
						URL originURL = ep.getEnclosure().getURL();
						com.kxtract.data.dao.Episode newEpisode = new com.kxtract.data.dao.Episode(p.getId(),
								ep.getTitle(), null, originURL.toString(), null, ep.getFileSizeInKB());

						episodesDownloaded.add(newEpisode);
						logger.info("Checking S3 before upload . . . .");
						String episodeFilename = ep.getFilename();
						URL s3URL = null;
						if (S3Uploader.fileAlreadyExistsInS3(RAW_AUDIO_BUCKET_NAME, episodeFilename)) {
							logger.warn("File (" + episodeFilename + ") already exists in S3 . . .");
							s3URL = S3Uploader.getObjectURL(RAW_AUDIO_BUCKET_NAME, episodeFilename);
						} else {
							logger.info("Uploading File (" + episodeFilename + ") to S3 . . . ");
							s3URL = S3Uploader.uploadFileToS3(RAW_AUDIO_BUCKET_NAME, new File(DOWNLOAD_PATH + episodeFilename));
							logger.info("File (" + episodeFilename + ") Upload complete!");
							episodesUploadedToS3.add(newEpisode);
							
							//String jobName = Transcriber.launchTranscriptionJob(RAW_AUDIO_BUCKET_NAME, episodeFilename,
							//	TRANSCRIPTION_BUCKET_NAME);
							//logger.info("Transcription Job Started --> " + jobName);
						}
						newEpisode.setProcessingStatus(ProcessingStatus.DOWNLOADED);
						newEpisode.setS3URL(s3URL.toString());
						episodeRepo.save(newEpisode);
						
					} else {
						logger.warn("Episode ( " + ep.getTitle() + ") was already in the database");
					}
				}
			} catch (Exception e) {
				logger.error("Exception " + e.getMessage(), e);
				String stackTrace = ExceptionUtils.getStackTrace(e);
				stackTraces.add(stackTrace);
			}
			podcastCount++;
			logger.info("Processed Podcast " + podcastCount + " of " + podcastList.size() + "");
		}
		logger.info("REFRESH COMPLETE!");
		model.addAttribute("episodeDownloads", episodesDownloaded);
		model.addAttribute("episodesUploadedToS3", episodesUploadedToS3);
		model.addAttribute("errorStackTraces", stackTraces);
		return "refresh";
	}
}
