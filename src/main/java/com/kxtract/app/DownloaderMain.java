package com.kxtract.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kxtract.podengine.models.Episode;
import com.kxtract.s3.S3Uploader;
import com.kxtract.transcription.Transcriber;

/**
 * TODO: Remove this class once RefreshController is operational
 * @author kellyfj
 */
public class DownloaderMain {
	private Logger logger = LoggerFactory.getLogger(DownloaderMain.class);
	private static final String DOWNLOAD_PATH = "/tmp/downloads/audio/";
	private static final String AUDIO_BUCKET_NAME = "kxtract";
	private static final String TRANSCRIPTION_BUCKET_NAME = "kxtract-transcriptions";
	
	public static void main(String[] args) {
		new DownloaderMain();
	}
	
	public DownloaderMain() {
		InputStream stream = this.getClass().getResourceAsStream("/podcasts.txt");
		int numberOfPodcastsChecked = 0;
		int numberOfNewEpisodesUploaded = 0;
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
			for (String rss; (rss = br.readLine()) != null;) {

				Episode episode = PodcastDownloader.downloadLatestEpisode(rss, DOWNLOAD_PATH, false);
				String episodeFilename = episode.getFilename();
				numberOfPodcastsChecked++;

				logger.info("Checking S3 before upload . . . .");
				if(S3Uploader.fileAlreadyExistsInS3(AUDIO_BUCKET_NAME, episodeFilename)) {
					logger.info("File already exists in S3 . . .");
				} else {
					logger.info("Uploading to S3 . . . ");
					S3Uploader.uploadFileToS3(AUDIO_BUCKET_NAME, new File(DOWNLOAD_PATH+episodeFilename));
					logger.info("Upload to S3 Completed!");
					numberOfNewEpisodesUploaded++;
					
					//Create Transcript
					//@TODO if transcription already downloaded then skip
					//@TODO if transcription already performed but NOT downloaded then ????
					String jobName = Transcriber.launchTranscriptionJob(AUDIO_BUCKET_NAME, episodeFilename,
							TRANSCRIPTION_BUCKET_NAME);
					String downloadUri = Transcriber.waitForTranscriptionJobToComplete(jobName);
					String[] filePathParts = episodeFilename.split("\\.");
					//String[] filePathParts = "jbp.mp3".split("\\.");
					File targetFile = new File("/tmp/downloads/text_transcript_"+filePathParts[0]+".json");
					//Download Transcript
					FileUtils.copyURLToFile(new URL(downloadUri), targetFile);
					StringBuilder sb = Transcriber.simplifyTranscription(targetFile.getAbsolutePath());
					Transcriber.writeStringToFile(sb, "/tmp/postprocessing/text_transcript_"+filePathParts[0]+"_simplified.txt");
				}
			}
			
			logger.info("Checked (" + numberOfPodcastsChecked + ") podcasts.");
			logger.info("Uploaded (" + numberOfNewEpisodesUploaded + ") new episodes");
			logger.info("DONE!");
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
