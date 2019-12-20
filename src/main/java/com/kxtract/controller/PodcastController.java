package com.kxtract.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kxtract.data.PodcastRepository;
import com.kxtract.data.dao.Podcast;

@RestController
public class PodcastController {

	@Autowired
	private PodcastRepository podRepo;
	
	@GetMapping("/api/podcasts")
	public List<Podcast> podcasts(Model model) {
		return (List<Podcast>) podRepo.findAll();
	}

}