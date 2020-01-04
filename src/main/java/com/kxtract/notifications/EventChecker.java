package com.kxtract.notifications;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kxtract.app.PodcastDownloader;
import com.kxtract.data.EpisodeRepository;
import com.kxtract.data.PodcastRepository;
import com.kxtract.data.dao.Episode;
import com.kxtract.data.dao.Podcast;

@Component
public class EventChecker {
	private Logger logger = LoggerFactory.getLogger(EventChecker.class);
	
	@Autowired
	private PodcastRepository podRepo;
	
	@Autowired
	private EpisodeRepository episodeRepo;
	
	public List<Podcast> checkForPodcastsWithNewEpisodes() {
		
		List<Podcast> podcastsWithNewEpisodes = new ArrayList<>();
		List<Podcast> podcastList = (List<Podcast>) podRepo.findAll();
		int podcastCount=0;
		for(Podcast p : podcastList) {
			podcastCount++;
			logger.info("Checking "+ podcastCount+ " of " + podcastList.size() + " Podcast " + p.getName());
			String episodeName = PodcastDownloader.getLatestEpisodeName(p.getRssURL());
			
			//Is that episode in our database already
			Episode found = episodeRepo.findByPodcastIdAndEpisodeName(p.getId(), episodeName);
			if(found != null) {
				logger.info("Episode ("+ episodeName + ") is already in the database");
			} else {
				logger.info("Episode ("+ episodeName + ") was not found in the database");
				podcastsWithNewEpisodes.add(p);
			}
		}
		return podcastsWithNewEpisodes;
	}
}
