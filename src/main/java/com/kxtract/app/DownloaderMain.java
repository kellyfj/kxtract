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

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.kxtract.podengine.models.Episode;
import com.kxtract.podengine.models.Podcast;
import com.kxtract.s3.S3Uploader;

public class DownloaderMain {

	public static void main(String[] args) {
		new DownloaderMain();
	}
	
	public DownloaderMain() {
		InputStream stream = this.getClass().getResourceAsStream("/podcasts.txt");
		int numberOfPodcastsChecked = 0;
		int numberOfEpisodesDownloaded = 0;
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
			for (String line; (line = br.readLine()) != null;) {
				Podcast podcast = new Podcast(new URL(line));
				numberOfPodcastsChecked++;
				
				List<Episode> episodes = podcast.getEpisodes();
				System.out.println("Podcast (" + podcast.getTitle() + ")");
				System.out.println("\t Has (" + podcast.getEpisodes().size() + ") episodes");
				Episode lastEpisode = episodes.get(0);
				System.out.println("\t Last Episode is named (" + lastEpisode.getTitle() + ") ");
				System.out.println("\t Published (" + daysDifferent(lastEpisode.getPubDate(), new Date()) + " days ago)");

				URL downloadURL = lastEpisode.getEnclosure().getURL();
				System.out.println("\t Download URL = " + downloadURL);
				
				String[] segments = downloadURL.getPath().split("/");
				String filename = segments[segments.length-1];
				
				System.out.println("\tfilename = " + filename);
				File f = new File("/tmp/downloads/" + filename);
				
				if(f.exists()) {
					System.out.println("File already exists locally . . . Skipping download");
				} else {
					System.out.println("Creating file " + f.getAbsolutePath() + " . . . .");
					FileUtils.copyURLToFile(downloadURL, f);
					numberOfEpisodesDownloaded++;
					System.out.println("File download Completed!");
				}
				
				String bucketName = "kxtract";
				System.out.println("Checking S3 before upload . . . .");
				if(S3Uploader.fileAlreadyExistsInS3(bucketName, filename)) {
					System.out.println("File already exists in S3 . . .");
				} else {
					System.out.println("Uploading to S3 . . . ");
					S3Uploader.uploadFileToS3(bucketName, f);
					System.out.println("Upload to S3 Completed!");
				}
			}
			
			System.out.println("Checked (" + numberOfPodcastsChecked + ") podcasts.");
			System.out.println("Downloaded (" + numberOfEpisodesDownloaded + ") episodes");
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
