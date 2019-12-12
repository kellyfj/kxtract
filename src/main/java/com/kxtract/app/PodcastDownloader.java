package com.kxtract.app;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import com.kxtract.podengine.models.Episode;
import com.kxtract.podengine.models.Podcast;

public class PodcastDownloader {

	public static String downloadLatestEpisode(String podcastName, String downloadPathname, boolean downloadIfAlreadyExists) {
		try {
			Podcast podcast = new Podcast(new URL(podcastName));

			List<Episode> episodes = podcast.getEpisodes();
			System.out.println("Podcast (" + podcast.getTitle() + ")");
			System.out.println("\t Has (" + podcast.getEpisodes().size() + ") episodes");
			Episode lastEpisode = episodes.get(0);
			System.out.println("\t Last Episode is named (" + lastEpisode.getTitle() + ") ");
			System.out.println("\t Published (" + daysDifferent(lastEpisode.getPubDate(), new Date()) + " days ago)");

			URL downloadURL = lastEpisode.getEnclosure().getURL();
			System.out.println("\t Download URL = " + downloadURL);

			String[] segments = downloadURL.getPath().split("/");
			String filename = segments[segments.length - 1];

			System.out.println("\tfilename = " + filename);
			File f = new File(downloadPathname + filename);

			if (f.exists() && !downloadIfAlreadyExists) {
				System.out.println("File already exists locally . . . Skipping download");
			} else {
				System.out.println("Creating file " + f.getAbsolutePath() + " . . . .");
				FileUtils.copyURLToFile(downloadURL, f);
				System.out.println("File download Completed!");
			}

			return filename;
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
