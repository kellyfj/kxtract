package com.kxtract.data;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.kxtract.data.dao.Podcast;

public interface PodcastRepository extends CrudRepository<Podcast, Integer>{
	  List<Podcast> findByName(String name);

	  Podcast findById(int id);
}