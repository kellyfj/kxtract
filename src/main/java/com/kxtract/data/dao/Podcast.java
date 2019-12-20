package com.kxtract.data.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Podcast {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  private String rss_url;

  protected Podcast() {}

  public Podcast(String name, String rss_url) {
	  this.name = name;
	  this.rss_url = rss_url;	  
  }
  
  @Override
  public String toString() {
    return String.format(
        "Podcast[id=%d, name='%s', rss_url='%s']",
        id, name, rss_url);
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getRssURL() {
    return rss_url;
  }
}