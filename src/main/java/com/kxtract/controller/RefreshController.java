package com.kxtract.controller;

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

@Controller
public class RefreshController {
	private Logger logger = LoggerFactory.getLogger(RefreshController.class);
	private static final String DOWNLOAD_PATH = "/tmp/downloads/audio/";

	@Autowired
	private PodcastRepository podRepo;

	@Autowired
	private EpisodeRepository episodeRepo;

	@GetMapping("/refresh")
	public String refreshLatestEpisodes(Model model) {
		List<com.kxtract.data.dao.Episode> episodesDownloaded = new ArrayList<>();

		List<Podcast> podcastList = (List<Podcast>) podRepo.findAll();
		for (Podcast p : podcastList) {
			Episode ep = PodcastDownloader.downloadLatestEpisode(p.getRssURL(), DOWNLOAD_PATH, false);
			if (ep != null) {

				//Confirm it already isn't in the database
				if (episodeRepo.findByPodcastIdAndEpisodeName(p.getId(), ep.getTitle()) == null) {
					com.kxtract.data.dao.Episode newEpisode = new com.kxtract.data.dao.Episode(p.getId(), ep.getTitle(),
							null, ep.getFileSizeInKB());
					episodeRepo.save(newEpisode);
					episodesDownloaded.add(newEpisode);
				} else {
					logger.warn("Episode ( " + ep.getTitle() + ") was already in the database");
				}

			}
		}
		model.addAttribute("episodeDownloads", episodesDownloaded);
		return "refresh";
	}
}
