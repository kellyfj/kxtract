package com.kxtract.data.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transcription {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int episodeId;

	private String s3_transcription_download_location;
	private String raw_transcription;
	private String formatted_transcription;

	public Transcription() {
		
	}
	
	public Transcription(int episodeId) {
		this.setEpisodeId(episodeId);
	}
	
	public int getId() {
		return id;
	}

	public String getS3_transcription_download_location() {
		return s3_transcription_download_location;
	}

	public void setS3_transcription_download_location(String s3_transcription_download_location) {
		this.s3_transcription_download_location = s3_transcription_download_location;
	}

	public String getRaw_transcription() {
		return raw_transcription;
	}

	public void setRaw_transcription(String raw_transcription) {
		this.raw_transcription = raw_transcription;
	}

	public String getFormatted_transcription() {
		return formatted_transcription;
	}

	public void setFormatted_transcription(String formatted_transcription) {
		this.formatted_transcription = formatted_transcription;
	}

	public int getEpisodeId() {
		return episodeId;
	}

	public void setEpisodeId(int episodeId) {
		this.episodeId = episodeId;
	}
}
