package com.kxtract.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import com.kxtract.podengine.models.Episode;
import com.kxtract.podengine.models.Podcast;

public class DownloaderMain {

	public static void main(String[] args) {
		new DownloaderMain();
	}

	
	public DownloaderMain() {
		InputStream stream = this.getClass().getResourceAsStream("/podcasts.txt");

		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
			for (String line; (line = br.readLine()) != null;) {
				Podcast podcast = new Podcast(new URL(line));
				List<Episode> episodes = podcast.getEpisodes();
				System.out.println("Podcast (" + podcast.getTitle() + ")");
				System.out.println("\t Has (" + podcast.getEpisodes().size() + ") episodes");
				Episode lastEpisode = episodes.get(0);
				System.out.println("\t Last Episode is named (" + lastEpisode.getTitle() + ") ");
				System.out.println("\t Published (" + daysDifferent(lastEpisode.getPubDate(), new Date()) + " days ago)");

				URL downloadURL = lastEpisode.getEnclosure().getURL();
				System.out.println("\t Download URL = " + downloadURL);
				String filename = downloadURL.getFile();
				System.out.println("\tfilename = " + filename);
				File f = new File("/tmp/" + filename);
				System.out.println("Creatig file " + f.getAbsolutePath() + " . . . .");
				FileUtils.copyURLToFile(downloadURL, f);
				System.out.println("Completed!");
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static long daysDifferent(Date d1, Date d2) {
		long diffInMillis = Math.abs(d2.getTime() - d1.getTime());
		long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
		return diffInDays;
	}
}
