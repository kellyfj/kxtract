package com.kxtract.transcription;

import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClient;
import com.amazonaws.services.transcribe.model.GetTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.LanguageCode;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.MediaFormat;
import com.amazonaws.services.transcribe.model.Settings;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.TranscriptionJob;
import com.amazonaws.services.transcribe.model.TranscriptionJobStatus;
import com.kxtract.s3.S3Uploader;

public class Transcriber {
	private static AmazonTranscribe client = AmazonTranscribeClient.builder().withRegion("us-east-1").build();
	
	public static String launchTranscriptionJob(String bucketName, String fileName) {
		StartTranscriptionJobRequest request = new StartTranscriptionJobRequest();
		Media media = new Media();

		media.setMediaFileUri(S3Uploader.getS3Filename(bucketName, fileName));

		// request.withMedia(media).withMediaSampleRateHertz(8000);
		request.setMedia(media);
		request.withLanguageCode(LanguageCode.EnUS);
		Settings settings = new Settings();
		settings.setMaxSpeakerLabels(3);
		settings.setShowSpeakerLabels(true);
		request.setSettings(settings);
		
		String transcriptionJobName = "myJob-" + fileName + "-" + System.currentTimeMillis(); // consider a unique name
																								// as an id.
		request.setTranscriptionJobName(transcriptionJobName);
		request.withMediaFormat(MediaFormat.Mp3);
		client.startTranscriptionJob(request);
		return transcriptionJobName;
	}
	
	public static String waitForTranscriptionJobToComplete(String transcriptionJobName) {
		GetTranscriptionJobRequest jobRequest = new GetTranscriptionJobRequest();
		jobRequest.setTranscriptionJobName(transcriptionJobName);
		TranscriptionJob transcriptionJob;

		// Max wait time = N minutes
		long MAX_WAIT_TIME_IN_MILLIS = 20 * 60 * 1000;
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < MAX_WAIT_TIME_IN_MILLIS) {
			transcriptionJob = client.getTranscriptionJob(jobRequest).getTranscriptionJob();
			String status = transcriptionJob.getTranscriptionJobStatus();
			System.out.println("Time expired: " + (System.currentTimeMillis() - startTime)/1000 + "s  Job Status: " + status);
			if (transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.COMPLETED.name())) {
				// transcription = this.download(
				// transcriptionJob.getTranscript().getTranscriptFileUri(), fileName);
				String transcriptFileURI = transcriptionJob.getTranscript().getTranscriptFileUri();
				System.out.println("Download Transcript from " + transcriptFileURI);
				return transcriptFileURI;
			} else if (transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.FAILED.name())) {
				System.err.println("Transcription Job Failed");
				return null;
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}
		return null;
	}
}
