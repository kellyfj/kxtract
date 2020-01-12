package com.kxtract.data;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.kxtract.data.dao.Transcription;

public interface TranscriptionRepository extends CrudRepository<Transcription, Integer>{
	  List<Transcription> findByEpisodeId(int episodeId);

	  Transcription findById(int id);
}
