package com.kxtract.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kxtract.data.PodcastRepository;
import com.kxtract.data.dao.Podcast;

@Controller
public class PodcastUIController {

	@Autowired
	private PodcastRepository podRepo;
	
	@GetMapping("/ui/podcasts")
	public String viewpodcasts(Model model) {
		
		List<Podcast> podcastList = (List<Podcast>) podRepo.findAll();
		model.addAttribute("list", podcastList);
		return "podcasts";
	}

}