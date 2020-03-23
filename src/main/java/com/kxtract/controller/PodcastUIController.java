package com.kxtract.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kxtract.data.EpisodeRepository;
import com.kxtract.data.PodcastRepository;
import com.kxtract.data.TranscriptionRepository;
import com.kxtract.data.dao.Episode;
import com.kxtract.data.dao.Podcast;
import com.kxtract.data.dao.Transcription;

@Controller
public class PodcastUIController {

	@Autowired
	private PodcastRepository podRepo;
	
	@Autowired
	private EpisodeRepository episodeRepo;
	
	@Autowired
	private TranscriptionRepository transcriptionRepo;
	
	@GetMapping("/")
	public String index(Model model) {
		return "index";
	}
	
    // Login form  
    @RequestMapping("/login.html")  
    public String login() {  
        return "login.html";  
    }  

    // Login form with error  
    @RequestMapping("/login-error.html")  
    public String loginError(Model model) {  
        model.addAttribute("loginError", true);  
        return "login.html";  
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
	
	@GetMapping("/ui/podcast/{podcastId}/episodes/{episodeId}/transcription")
	public String transcription(@PathVariable int podcastId, @PathVariable int episodeId, Model model) {
		Transcription t = transcriptionRepo.findByEpisodeId(episodeId);
		Podcast p = podRepo.findById(podcastId);
		Episode e = episodeRepo.findById(episodeId);
		
		model.addAttribute("podcastName", p.getName());
		model.addAttribute("episodeName", e.getEpisodeName());
		
		model.addAttribute("transcription", t);
		return "transcriptions";
	}
	
	/**
	 * Just to help test the error page
	 */
	@GetMapping("/ui/testerror")
	  public void handleRequest() {
	      throw new RuntimeException("test exception");
	  }

}