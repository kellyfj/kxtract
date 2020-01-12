package com.kxtract.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TranscribeController {
	private Logger logger = LoggerFactory.getLogger(TranscribeController.class);
	
	@PostMapping(value="/transcribe")
	public ModelAndView transcribe(@RequestParam String id) {
		logger.info("Transcribing episodeId (" + id + ")");
		
		return new ModelAndView("redirect:/ui/episodes");
	}
}
