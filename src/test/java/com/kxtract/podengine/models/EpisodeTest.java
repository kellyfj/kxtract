package com.kxtract.podengine.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.kxtract.podengine.exceptions.DateFormatException;
import com.kxtract.podengine.exceptions.MalformedFeedException;
import com.kxtract.podengine.models.Episode.Enclosure;
import com.kxtract.podengine.utils.DateUtils;

public class EpisodeTest {

	@Test
	public void testEpisodeMethods() throws MalformedFeedException, MalformedURLException, DateFormatException {
		Podcast p = new Podcast(PodcastTest.EXAMPLE_RSS_XML_COMPLEX);
		
		List<Episode> episodes = p.getEpisodes();
		
		for(Episode e : episodes) {
			String author = e.getAuthor();
			assertNull(author);
			Set<String> categories = e.getCategories();
			assertNotNull(categories);
			assertFalse(categories.isEmpty());
			URL comments = e.getComments();
			assertNotNull(comments);
			String contentEncoded = e.getContentEncoded();
			assertNull(contentEncoded);
			String description = e.getDescription();
			assertNotNull(description);
			Enclosure enc = e.getEnclosure();
			assertNull(enc);
			String filename = e.getFilename();
			assertNull(filename);
			String guid = e.getGUID();
			assertNull(guid);
			ITunesItemInfo itunes = e.getITunesInfo();
			assertNotNull(itunes);
			URL link = e.getLink();
			assertNotNull(link);
			Date d = e.getPubDate();
			assertNotNull(d);
			String sourceName = e.getSourceName();
			assertNull(sourceName);
			URL sourceURL = e.getSourceURL();
			assertNull(sourceURL);
			String t = e.getTitle();
			assertNotNull(t);		
		}
		
	}
	
	private static Podcast podcast;

    @BeforeAll
    public static void setup() throws Exception {
    	String source = readLineByLineJava8("/testfeed.rss");
        podcast = new Podcast(source);
    }
    
	private static String readLineByLineJava8(String classpath) {
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(PodcastTest.class.getResource(classpath).toURI()),
				StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}

		return contentBuilder.toString();
	}

    @Test
    public void testEpisode() throws MalformedFeedException, MalformedURLException, DateFormatException {
        List<Episode> episodes = podcast.getEpisodes();
        Episode episode = episodes.get(0);

        assertEquals("Episode 1: Are you not getting bored yet?", episode.getTitle());
        assertEquals("Our hosts start getting bored of running a testing podcast feed. There's probably some moaning about Apple too. This is a technology podcast after all.", episode.getDescription());
        assertEquals("https://podcast-feed-library.owl.im/episodes/1", episode.getLink().toString());
        assertEquals("Icosillion", episode.getAuthor());
        Set<String> categories = episode.getCategories();
        assertTrue(categories.contains("Technology"));
        assertTrue(categories.contains("Testing"));
        assertEquals("https://podcast-feed-library.owl.im/episodes/1/comments", episode.getComments().toString());
        assertEquals("https://podcast-feed-library.owl.im/episodes/1", episode.getGUID());
        assertEquals(DateUtils.stringToDate("Mon, 28 Nov 2016 13:30:00 GMT"), episode.getPubDate());
        assertEquals("Master Feed", episode.getSourceName());
        assertEquals("http://podcast-feed-library.owl.im/feed.rss", episode.getSourceURL().toString());
        assertEquals("Our hosts start getting bored of running a testing podcast feed. There's probably some moaning about Apple too. This is a technology podcast after all.\n" +
                "                The show notes live in this section, but we have nothing else interesting to say.\n" +
                "            ", episode.getContentEncoded());

        //Enclosure
        Episode.Enclosure enclosure = episode.getEnclosure();
        assertEquals("https://podcast-feed-library.owl.im/audio/episode-1.mp3", enclosure.getURL().toString());
        assertEquals(1234000L, (long) enclosure.getLength());
        assertEquals("audio/mp3", enclosure.getType());

        //iTunes Info
        ITunesItemInfo iTunesInfo = episode.getITunesInfo();
        assertEquals("Icosillion", iTunesInfo.getAuthor());
        assertEquals("Our hosts start getting bored of running a testing podcast feed. There's probably some moaning about Apple too. This is a technology podcast after all.", iTunesInfo.getSubtitle());
        assertEquals("Our hosts start getting bored of running a testing podcast feed. There's probably some moaning about Apple too. This is a technology podcast after all.", iTunesInfo.getSummary());
        assertFalse(iTunesInfo.isBlocked());
        assertEquals(ITunesInfo.ExplicitLevel.CLEAN, iTunesInfo.getExplicit());
        assertEquals("https://podcast-feed-library.owl.im/images/artwork.png", iTunesInfo.getImage().toString());
        assertEquals("12:34", iTunesInfo.getDuration());
        assertFalse(iTunesInfo.isClosedCaptioned());
        assertEquals(1, (int) iTunesInfo.getOrder());
        assertEquals(1, (int) iTunesInfo.getSeasonNumber());
        assertEquals(1, (int) iTunesInfo.getEpisodeNumber());
    }
}
