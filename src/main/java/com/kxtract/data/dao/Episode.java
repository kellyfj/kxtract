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
	  private int episodeId;
	  private int podcastId;
	  private String episodeName;
	  
	  protected Episode() {}

	  public Episode(int episodeId, int podcastId, String episodeName) {
		  this.episodeId = episodeId;
		  this.podcastId = podcastId;	  
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

	  public int getEpisodeId() {
	    return episodeId;
	  }
	  
	  public int getPodcastId() {
		  return podcastId;
	  } 
	  
	  public String getEpisodeName() {
		  return episodeName;
	  }
}
