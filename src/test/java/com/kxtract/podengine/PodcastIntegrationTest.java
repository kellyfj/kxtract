package com.kxtract.podengine;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.kxtract.podengine.models.Episode;
import com.kxtract.podengine.models.Podcast;

public class PodcastIntegrationTest {

	
	@Test
	public void testReadPodcastEpisodes() throws Exception {
		Podcast podcast = new Podcast(new URL("https://www.coachingsoccerweekly.com/feed/podcast/"));

		//Display Feed Details
		List<Episode> episodes = podcast.getEpisodes();
		assert(episodes.size() > 0);
		
		//List all episodes
		for (Episode episode : episodes) {
			assertNotNull(episode.getTitle());
		}
	}
	
	@Test
	public void testReadMultiplePodcasts() throws Exception {
		
		InputStream stream = this.getClass().getResourceAsStream("/podcasts.txt");
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
		    for(String line; (line = br.readLine()) != null; ) {
		       Podcast podcast = new Podcast(new URL(line));
		       List<Episode> episodes = podcast.getEpisodes();
		       System.out.println("Podcast (" + podcast.getTitle() + ") has (" + podcast.getEpisodes().size() + ") episodes");
		       Episode lastEpisode = episodes.get(0);
		       System.out.println("Last Episode is named (" + lastEpisode.getTitle() + ") Published on (" + lastEpisode.getPubDate() +")");
		      
		       
		       URL downloadURL = lastEpisode.getEnclosure().getURL();
		       System.out.println("Enclosure URL = " + downloadURL);
		    }
		}
	}
}
