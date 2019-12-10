package com.kxtract.podengine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.kxtract.podengine.exceptions.InvalidFeedException;
import com.kxtract.podengine.exceptions.MalformedFeedException;
import com.kxtract.podengine.models.Episode;
import com.kxtract.podengine.models.Podcast;

public class PodengineMain {

	public static void main(String[] args) throws MalformedURLException, InvalidFeedException, MalformedFeedException {
		Podcast podcast = new Podcast(new URL("https://www.relay.fm/cortex/feed"));

		//Display Feed Details
		List<Episode> episodes = podcast.getEpisodes();
		System.out.printf("💼 %s has %d episodes!\n", podcast.getTitle(), episodes.size());

		//List all episodes
		for (Episode episode : episodes) {
			System.out.println("- " + episode.getTitle());
		}

	}

}
