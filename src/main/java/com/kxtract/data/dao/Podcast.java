package com.kxtract.data.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Podcast {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY) 
  private Integer id;
  private String name;
  private String rss_url;

  protected Podcast() {}

  public Podcast(Integer id, String name, String rss_url) {
	  this.id = id;
	  this.name = name;
	  this.rss_url = rss_url;	  
  }
  
  @Override
  public String toString() {
    return String.format(
        "Podcast[id=%d, name='%s', rss_url='%s']",
        id, name, rss_url);
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getRssURL() {
    return rss_url;
  }
}