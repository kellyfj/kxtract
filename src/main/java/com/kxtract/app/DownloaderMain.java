package com.kxtract.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.kxtract.s3.S3Uploader;

public class DownloaderMain {

	public static void main(String[] args) {
		new DownloaderMain();
	}
	
	public DownloaderMain() {
		InputStream stream = this.getClass().getResourceAsStream("/podcasts.txt");
		int numberOfPodcastsChecked = 0;
		int numberOfNewEpisodesUploaded = 0;
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
			for (String rss; (rss = br.readLine()) != null;) {

				String episodeFilename = PodcastDownloader.downloadLatestEpisode(rss, "/tmp/downloads/", false);
				numberOfPodcastsChecked++;
				String bucketName = "kxtract";
				System.out.println("Checking S3 before upload . . . .");
				if(S3Uploader.fileAlreadyExistsInS3(bucketName, episodeFilename)) {
					System.out.println("File already exists in S3 . . .");
				} else {
					System.out.println("Uploading to S3 . . . ");
					S3Uploader.uploadFileToS3(bucketName, new File(episodeFilename));
					System.out.println("Upload to S3 Completed!");
					numberOfNewEpisodesUploaded++;
				}
			}
			
			System.out.println("Checked (" + numberOfPodcastsChecked + ") podcasts.");
			System.out.println("Uploaded (" + numberOfNewEpisodesUploaded + ") new episodes");
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
