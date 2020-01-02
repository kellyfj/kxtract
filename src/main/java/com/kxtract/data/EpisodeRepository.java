package com.kxtract.data;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.kxtract.data.dao.Episode;

public interface EpisodeRepository extends CrudRepository<Episode, Integer>{
	  List<Episode> findByPodcastId(int podcastId);

	  Episode findById(int id);
	  
	  Episode findByPodcastIdAndEpisodeName(int podcastId, String name);
}
