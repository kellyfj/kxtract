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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.*;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
				
				String[] segments = downloadURL.getPath().split("/");
				String filename = segments[segments.length-1];
				
				System.out.println("\tfilename = " + filename);
				File f = new File("/tmp/downloads/" + filename);
				
				if(f.exists()) {
					System.out.println("File already exists locally . . . Skipping download");
				} else {
					System.out.println("Creating file " + f.getAbsolutePath() + " . . . .");
					FileUtils.copyURLToFile(downloadURL, f);
					System.out.println("File download Completed!");
				}
				
				String bucketName = "kxtract";
				System.out.println("Checking S3 before upload . . . .");
				if(fileAlreadyExistsInS3(bucketName, filename)) {
					System.out.println("File already exists in S3 . . .");
				} else {
					System.out.println("Uploading to S3 . . . ");
					uploadFileToS3(bucketName, f);
					System.out.println("Upload to S3 Completed!");
				}
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean fileAlreadyExistsInS3(String bucketName, String fileName) {
        AmazonS3 s3 =  AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();      
        try {
            boolean exists = s3.doesObjectExist(bucketName, fileName);
            return exists;      
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }
    }
	
	public static void uploadFileToS3(String bucketName, File f) {
		AmazonS3 s3 =  AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();   
		String key = f.getName();
		PutObjectRequest p = new PutObjectRequest(bucketName, f.getName(), f);
		
		// Put Object
		s3.putObject(p);
	}
	
	public static long daysDifferent(Date d1, Date d2) {
		long diffInMillis = Math.abs(d2.getTime() - d1.getTime());
		long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
		return diffInDays;
	}
}
