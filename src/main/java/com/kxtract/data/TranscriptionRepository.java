package com.kxtract.data;

import org.springframework.data.repository.CrudRepository;

import com.kxtract.data.dao.Transcription;

public interface TranscriptionRepository extends CrudRepository<Transcription, Integer>{
	  Transcription findByEpisodeId(int episodeId);

	  Transcription findById(int id);
}
