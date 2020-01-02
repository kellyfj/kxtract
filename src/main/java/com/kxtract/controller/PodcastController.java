package com.kxtract.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kxtract.data.EpisodeRepository;
import com.kxtract.data.PodcastRepository;
import com.kxtract.data.dao.Episode;
import com.kxtract.data.dao.Podcast;

@RestController
public class PodcastController {

	@Autowired
	private PodcastRepository podRepo;
	
	@Autowired
	private EpisodeRepository episodeRepo;
	
	@GetMapping("/api/podcasts")
	public List<Podcast> podcasts(Model model) {
		return (List<Podcast>) podRepo.findAll();
	}

	@GetMapping("/api/episodes")
	public List<Episode> episodes(Model model) {
		return (List<Episode>) episodeRepo.findAll();
	}
	
	@GetMapping("/api/podcast/{podcastId}/episodes")
	public List<Episode> episodes(@PathVariable int podcastId, Model model) {
		return (List<Episode>) episodeRepo.findByPodcastId(podcastId);
	}

}