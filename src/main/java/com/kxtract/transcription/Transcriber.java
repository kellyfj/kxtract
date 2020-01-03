package com.kxtract.transcription;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kxtract.s3.S3Uploader;

public class Transcriber {
	private static Logger logger = LoggerFactory.getLogger(Transcriber.class);
	private static AmazonTranscribe client = AmazonTranscribeClient.builder().withRegion("us-east-1").build();
	
	public static String launchTranscriptionJob(String inputBucketName, String fileName, String outputBucketName) {
		StartTranscriptionJobRequest request = new StartTranscriptionJobRequest();
		Media media = new Media();

		media.setMediaFileUri(S3Uploader.getS3Filename(inputBucketName, fileName));

		// request.withMedia(media).withMediaSampleRateHertz(8000);
		request.setMedia(media);
		request.withLanguageCode(LanguageCode.EnUS);
		request.setOutputBucketName(outputBucketName);
		Settings settings = new Settings();
		settings.setMaxSpeakerLabels(3); //Without this we can't tell which speaker said what
		settings.setShowSpeakerLabels(true);
		request.setSettings(settings);
		
		String transcriptionJobName = "myJob-" + fileName + "-" + System.currentTimeMillis(); // consider a unique name
																								// as an id.
		request.setTranscriptionJobName(transcriptionJobName);
		if (fileName.endsWith(".mp3")) {
			request.withMediaFormat(MediaFormat.Mp3);
		} else if (fileName.endsWith(".m4a")) {
			request.withMediaFormat(MediaFormat.Mp4);
		} else
			throw new IllegalArgumentException("File Type unknown based on Filename ( " + fileName + ")");
		
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
			logger.info("Time expired: " + (System.currentTimeMillis() - startTime)/1000 + "s  Job Status: " + status);
			if (transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.COMPLETED.name())) {
				// transcription = this.download(
				// transcriptionJob.getTranscript().getTranscriptFileUri(), fileName);
				String transcriptFileURI = transcriptionJob.getTranscript().getTranscriptFileUri();
				logger.info("Download Transcript from " + transcriptFileURI);
				return transcriptFileURI;
			} else if (transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.FAILED.name())) {
				logger.error("Transcription Job Failed");
				return null;
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}
		return null;
	}
	
	/**
	 * Logic built on https://github.com/trhr/aws-transcribe-transcript/blob/master/transcript.py
	 */
	public static StringBuilder simplifyTranscription(String filePath) throws IOException {
		String rawJSON = readFileToString(filePath);		
		logger.debug(rawJSON);
		ObjectMapper objectMapper = new ObjectMapper();
		Transcription transcription = objectMapper.readValue(rawJSON, Transcription.class);
		logger.info("Read and parsed JSON output");
		
		List<Segment> segments = transcription.getResults().getSpeaker_labels().getSegments();
		
		Map<Double, String> speakerStartTimes = new HashMap<Double, String>();
		for(Segment segment : segments) {
			for(SegmentItems item : segment.getItems()) {
					Double startTime = item.getStart_time();
					String speakerLabel = item.getSpeaker_label();
					speakerStartTimes.put(startTime, speakerLabel);
			}
		}
		
		List<Item> items = transcription.getResults().getItems();
		StringBuilder line = new StringBuilder();
		StringBuilder result = new StringBuilder();
		Double time = 0.0;
		String speaker = null;
		for(Item item : items) {
			String content = item.getAlternatives().get(0).getContent();
			Double startTime = item.getStart_time();
			
			String currentSpeaker = null;
			if(startTime != null) {
				currentSpeaker = speakerStartTimes.get(startTime);
			} else if("punctuation".equals(item.getType())) {
				line.append(content);
			}
			
			if(currentSpeaker!=null && !currentSpeaker.equals(speaker)) {
				if(speaker != null) {
					result.append("(" + time + ") speaker:" + speaker + " --> " + line + "\n");
				}
				
				line = new StringBuilder(content);
				speaker = currentSpeaker;
				time = startTime;
				
			} else if (!"punctuation".equals(item.getType())) {
				line.append(" ").append(content);
			}	
		}
		result.append("(" + time + ") speaker:" + speaker + " --> " + line + "\n");
		
		return result;
	}
	
	public static void writeStringToFile(StringBuilder sb, String filePath) throws IOException {
		File file = new File(filePath);
		//Create directories above if they do not exist
		file.getParentFile().mkdirs();
		BufferedWriter writer = null;
		try {
		    writer = new BufferedWriter(new FileWriter(file));
		    writer.write(sb.toString());
		    logger.info("Wrote output to " + filePath);
		} finally {
		    if (writer != null) writer.close();
		}
	}
	
	private static String readFileToString(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();

		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return contentBuilder.toString();
	}
}
