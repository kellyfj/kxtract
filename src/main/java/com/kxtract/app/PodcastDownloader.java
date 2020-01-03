package com.kxtract.app;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kxtract.podengine.models.Episode;
import com.kxtract.podengine.models.Podcast;

public class PodcastDownloader {
	private static Logger logger = LoggerFactory.getLogger(PodcastDownloader.class);
	
	public static Episode downloadLatestEpisode(String podcastName, String downloadPathname, boolean downloadIfAlreadyExists) {
		try {
			Podcast podcast = new Podcast(new URL(podcastName));

			List<Episode> episodes = podcast.getEpisodes();
			logger.info("Podcast (" + podcast.getTitle() + ")");
			logger.info("\t Has (" + podcast.getEpisodes().size() + ") episodes");
			Episode lastEpisode = episodes.get(0);
			logger.info("\t Last Episode is named (" + lastEpisode.getTitle() + ") ");
			logger.info("\t Published (" + daysDifferent(lastEpisode.getPubDate(), new Date()) + " days ago)");

			URL downloadURL = lastEpisode.getEnclosure().getURL();
			logger.info("\t Download URL = " + downloadURL);
			
			String filename = lastEpisode.getFilename();
			File f = new File(downloadPathname + filename);

			if (f.exists() && !downloadIfAlreadyExists) {
				logger.info("File already exists locally . . . Skipping download");
				return null;
			} else {
				logger.info("Creating file " + f.getAbsolutePath() + " . . . .");
				FileUtils.copyURLToFile(downloadURL, f);
				logger.info("File download Completed! File size is " + f.length()/1024 + "kb");
				lastEpisode.setFileSizeInKB(f.length()/1024);
				return lastEpisode;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static long daysDifferent(Date d1, Date d2) {
		long diffInMillis = Math.abs(d2.getTime() - d1.getTime());
		long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
		return diffInDays;
	}
}
