package com.kxtract.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kxtract.data.EpisodeRepository;
import com.kxtract.data.PodcastRepository;
import com.kxtract.data.dao.Episode;
import com.kxtract.data.dao.Podcast;

@Controller
public class PodcastUIController {

	@Autowired
	private PodcastRepository podRepo;
	
	@Autowired
	private EpisodeRepository episodeRepo;
	
	@GetMapping("/")
	public String index(Model model) {
		return "index";
	}
	
	@GetMapping("/ui/podcasts")
	public String viewpodcasts(Model model) {
		
		List<Podcast> podcastList = (List<Podcast>) podRepo.findAll();
		model.addAttribute("list", podcastList);
		return "podcasts";
	}
	
	@GetMapping("/ui/episodes")
	public String episodes(Model model) {
		List<Episode> episodeList = (List<Episode>) episodeRepo.findAll();
		model.addAttribute("podcastId", "ALL");
		model.addAttribute("episodeList", episodeList);
		return "episodes";
	}
	
	@GetMapping("/ui/podcast/{podcastId}/episodes")
	public String episodes(@PathVariable int podcastId, Model model) {
		Podcast p = podRepo.findById(podcastId);
		List<Episode> episodeList = (List<Episode>) episodeRepo.findByPodcastId(podcastId);
		model.addAttribute("podcastId", p.getName());
		model.addAttribute("episodeList", episodeList);
		return "episodes";
	}

}