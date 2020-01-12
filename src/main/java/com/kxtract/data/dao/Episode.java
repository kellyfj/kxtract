package com.kxtract.data.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Episode {

	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private int id;
	  private Integer episodeId;
	  private int podcastId;
	  private String episodeName;
	  private Long filesize_KB;
	  private String origin_url;
	  private String s3_url;
	  private ProcessingStatus processingStatus;
	  
	  protected Episode() {}

	  public Episode(int podcastId, String episodeName, Integer episodeId, String originURL, String s3URL, Long fileSizeInKB) {
		  this.podcastId = podcastId;
		  this.episodeId = episodeId;
		  this.episodeName = episodeName;
		  this.origin_url = originURL;
		  this.s3_url = s3URL;
		  this.filesize_KB = fileSizeInKB;
	  }
	  
	  @Override
	  public String toString() {
	    return String.format(
	        "Episode[id=%d, episodeId='%s', podcastId='%s', episodeName='%s']",
	        id, episodeId, podcastId, episodeName);
	  }

	public int getId() {
		return id;
	}

	public Integer getEpisodeId() {
		return episodeId;
	}

	public int getPodcastId() {
		return podcastId;
	}

	public String getEpisodeName() {
		return episodeName;
	}

	public Long getFileSizeInKB() {
		return filesize_KB;
	}
	
	public String getOriginURL() {
		return origin_url;
	}

	public void setS3URL(String url) {
		s3_url = url;
	}
	
	public String getS3URL() {
		return s3_url;
	}
	
	public boolean isCanBeTranscribed() {
		return processingStatus.equals(ProcessingStatus.DOWNLOADED);
	}
	
	public boolean isTranscriptionAvailable() {
		return processingStatus.equals(ProcessingStatus.TRANSCRIPTION_COMPLETE);
	}
	
	public void setProcessingStatus(ProcessingStatus status) {
		this.processingStatus = status;
	}
	
	public ProcessingStatus getProcessingStatus() {
		return processingStatus;
	}
	
	public enum ProcessingStatus {
		DOWNLOADED,
		TRANSCRIPTION_STARTED,
		TRANSCRIPTION_COMPLETE,
		ERROR
	}
}


