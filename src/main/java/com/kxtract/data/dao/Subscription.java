package com.kxtract.data.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Subscription {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private int id;  
	  private String userId;
	  private int podcastId;
	  
	  protected Subscription() {}

	  public Subscription(String userId, int podcastId) {
		  this.userId = userId;
		  this.podcastId = podcastId;	  
	  }
	  
	  @Override
	  public String toString() {
	    return String.format(
	        "Subscription[id=%d, userId='%s', podcastId='%s']",
	        id, userId, podcastId);
	  }

	  public int getId() {
	    return id;
	  }

	  public String getUserId() {
	    return userId;
	  }
	  
	  public int getPodcastId() {
		  return podcastId;
	  } 
}