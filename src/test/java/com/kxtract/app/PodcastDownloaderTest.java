package com.kxtract.app;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.thymeleaf.util.StringUtils;

public class PodcastDownloaderTest {

	@Test
	public void testGetLatestEpisodeName() throws UnsupportedEncodingException, IOException {
		InputStream stream = this.getClass().getResourceAsStream("/podcasts.txt");

		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
			for (String rss; (rss = br.readLine()) != null;) {
					String latestName = PodcastDownloader.getLatestEpisodeName(rss);
					
					assertNotNull(latestName);
					assertFalse(StringUtils.isEmpty(latestName));
					System.out.println(rss + " --> " + latestName);
			}
		}
	}
}
