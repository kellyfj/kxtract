package com.kxtract.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kxtract.app.PodcastDownloader;
import com.kxtract.data.EpisodeRepository;
import com.kxtract.data.PodcastRepository;
import com.kxtract.data.dao.Podcast;
import com.kxtract.podengine.models.Episode;
import com.kxtract.s3.S3Uploader;

@Controller
public class RefreshController {
	private Logger logger = LoggerFactory.getLogger(RefreshController.class);
	private static final String DOWNLOAD_PATH = "/tmp/downloads/audio/";
	private static final String RAW_AUDIO_BUCKET_NAME = "kxtract";

	@Autowired
	private PodcastRepository podRepo;

	@Autowired
	private EpisodeRepository episodeRepo;

	@GetMapping("/refresh")
	public String refreshLatestEpisodes(Model model) {
		List<com.kxtract.data.dao.Episode> episodesDownloaded = new ArrayList<>();
		List<com.kxtract.data.dao.Episode> episodesUploadedToS3 = new ArrayList<>();

		List<Podcast> podcastList = (List<Podcast>) podRepo.findAll();
		for (Podcast p : podcastList) {
			Episode ep = PodcastDownloader.downloadLatestEpisode(p.getRssURL(), DOWNLOAD_PATH, false);
			
			//IF episode downloaded THEN
			if (ep != null) {

				//Confirm it already isn't in the database
				if (episodeRepo.findByPodcastIdAndEpisodeName(p.getId(), ep.getTitle()) == null) {
					URL url =  ep.getEnclosure().getURL();
					com.kxtract.data.dao.Episode newEpisode = new com.kxtract.data.dao.Episode(p.getId(), ep.getTitle(),
							null, url.toString(), ep.getFileSizeInKB());
					
					episodesDownloaded.add(newEpisode);					
					logger.info("Checking S3 before upload . . . .");
					String episodeFilename = ep.getFilename();
					if(S3Uploader.fileAlreadyExistsInS3(RAW_AUDIO_BUCKET_NAME, episodeFilename)) {
						logger.warn("File (" + episodeFilename + ") already exists in S3 . . .");
					} else {
						logger.info("Uploading File (" + episodeFilename + ") to S3 . . . ");
						S3Uploader.uploadFileToS3(RAW_AUDIO_BUCKET_NAME, new File(DOWNLOAD_PATH+episodeFilename));
						logger.info("File (" + episodeFilename + ") Upload complete!");
						episodesUploadedToS3.add(newEpisode);
					}
					episodeRepo.save(newEpisode);

				} else {
					logger.warn("Episode ( " + ep.getTitle() + ") was already in the database");
				}
			}
		}
		model.addAttribute("episodeDownloads", episodesDownloaded);
		model.addAttribute("episodesUploadedToS3", episodesUploadedToS3);
		return "refresh";
	}
}
