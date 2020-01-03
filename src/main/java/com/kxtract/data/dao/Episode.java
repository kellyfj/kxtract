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
	  
	  protected Episode() {}

	  public Episode(int podcastId, String episodeName, Integer episodeId, Long fileSizeInKB) {
		  this.podcastId = podcastId;
		  this.episodeId = episodeId;
		  this.episodeName = episodeName;
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

}
