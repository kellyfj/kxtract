package com.kxtract.podengine.models;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kxtract.podengine.exceptions.DateFormatException;
import com.kxtract.podengine.exceptions.MalformedFeedException;
import com.kxtract.podengine.utils.DateUtils;

public class PodcastTest {

	@Test
	public void testCtor_nullXML() throws MalformedFeedException {
		String xml = null;
		
		assertThrows(IllegalArgumentException.class,
	            ()->{new Podcast(xml);} );
	}
	
	@Test
	public void testCtor_blankStringXML() throws MalformedFeedException {
		String xml = "";
		
		assertThrows(MalformedFeedException.class,
	            ()->{new Podcast(xml);} );
	}
	
	@Test
	public void testCtor_SampleXML() throws MalformedFeedException {
		new Podcast(EXAMPLE_RSS_XML_SIMPLE);
	}
	
	@Test
	public void testCtor_SampleXML_Complex() throws MalformedFeedException, MalformedURLException, DateFormatException {
		Podcast p = new Podcast(EXAMPLE_RSS_XML_COMPLEX);
		String[] categories = p.getCategories();
		assertNotNull(categories);
		assertEquals(1, categories.length);
		
		CloudInfo c = p.getCloud();
		assertNull(c);
		
		String copyright = p.getCopyright();
		assertNotNull(copyright);
		
		String description = p.getDescription();
		assertNotNull(description);
		
		URL d = p.getDocs();
		assertNotNull(d);
		
		String docsString = p.getDocsString();
		assertNotNull(docsString);
		
		List<Episode> episodes = p.getEpisodes();
		assertNotNull(episodes);
		assertEquals(9, episodes.size());
		
		URL feedURL = p.getFeedURL();
		assertNull(feedURL);
		
		String generator = p.getGenerator();
		assertNotNull(generator);
		
		URL imageURL = p.getImageURL();
		assertNotNull(imageURL);
		
		ITunesChannelInfo itunes = p.getITunesInfo();
		assertNotNull(itunes);
		
		String[] keywords = p.getKeywords();
		assertNotNull(keywords);
		assertTrue(keywords.length == 0);
		
		String language = p.getLanguage();
		assertNotNull(language);
		
		Date lastBuildDate = p.getLastBuildDate();
		assertNotNull(lastBuildDate);
		
		String lastBuildDateString = p.getLastBuildDateString();
		assertNotNull(lastBuildDateString);
		
		URL link = p.getLink();
		assertNotNull(link);
		
		String managingEditor = p.getManagingEditor();
		assertNotNull(managingEditor);
		
		String PICSrating = p.getPICSRating();
		assertNull(PICSrating);
		
		Date pubDate = p.getPubDate();
		assertNotNull(pubDate);
		
		String pubDateString = p.getPubDateString();
		assertNotNull(pubDateString);
		
		Set<String> s=  p.getSkipDays();
		assertNull(s);
		
		Set<Integer> skipHours = p.getSkipHours();
		assertNull(skipHours);
		
		TextInputInfo t = p.getTextInput();
		assertNull(t);
		
		String title = p.getTitle();
		assertNotNull(title);
		
		Integer ttl = p.getTTL();
		assertNull(ttl);
		
		String webmaster = p.getWebMaster();
		assertNotNull(webmaster);
		
		String xml = p.getXMLData();
		assertNotNull(xml);
		assertEquals(EXAMPLE_RSS_XML_COMPLEX, xml);
	}
	
	//Sample XML from https://www.w3schools.com/xml/xml_rss.asp
	private static final String EXAMPLE_RSS_XML_SIMPLE = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + 
			"<rss version=\"2.0\">\n" + 
			"<channel>\n" + 
			"  <title>W3Schools Home Page</title>\n" + 
			"  <link>https://www.w3schools.com</link>\n" + 
			"  <description>Free web building tutorials</description>\n" + 
			"  <item>\n" + 
			"    <title>RSS Tutorial</title>\n" + 
			"    <link>https://www.w3schools.com/xml/xml_rss.asp</link>\n" + 
			"    <description>New RSS tutorial on W3Schools</description>\n" + 
			"  </item>\n" + 
			"  <item>\n" + 
			"    <title>XML Tutorial</title>\n" + 
			"    <link>https://www.w3schools.com/xml</link>\n" + 
			"    <description>New XML tutorial on W3Schools</description>\n" + 
			"  </item>\n" + 
			"</channel>\n" +  
			"</rss>";
	
	//Sample XML from here https://www.feedforall.com/sample.xml with a few tweaks for XML correctness (around font/<i> tag order)
	public static final String EXAMPLE_RSS_XML_COMPLEX = "<rss version=\"2.0\">\n" + 
			"<channel>\n" + 
			"<title>FeedForAll Sample Feed</title>\n" + 
			"<description>\n" + 
			"RSS is a fascinating technology. The uses for RSS are expanding daily. Take a closer look at how various industries are using the benefits of RSS in their businesses.\n" + 
			"</description>\n" + 
			"<link>http://www.feedforall.com/industry-solutions.htm</link>\n" + 
			"<category domain=\"www.dmoz.com\">\n" + 
			"Computers/Software/Internet/Site Management/Content Management\n" + 
			"</category>\n" + 
			"<copyright>Copyright 2004 NotePage, Inc.</copyright>\n" + 
			"<docs>http://blogs.law.harvard.edu/tech/rss</docs>\n" + 
			"<language>en-us</language>\n" + 
			"<lastBuildDate>Tue, 19 Oct 2004 13:39:14 -0400</lastBuildDate>\n" + 
			"<managingEditor>marketing@feedforall.com</managingEditor>\n" + 
			"<pubDate>Tue, 19 Oct 2004 13:38:55 -0400</pubDate>\n" + 
			"<webMaster>webmaster@feedforall.com</webMaster>\n" + 
			"<generator>FeedForAll Beta1 (0.0.1.8)</generator>\n" + 
			"<image>\n" + 
			"<url>http://www.feedforall.com/ffalogo48x48.gif</url>\n" + 
			"<title>FeedForAll Sample Feed</title>\n" + 
			"<link>http://www.feedforall.com/industry-solutions.htm</link>\n" + 
			"<description>FeedForAll Sample Feed</description>\n" + 
			"<width>48</width>\n" + 
			"<height>48</height>\n" + 
			"</image>\n" + 
			"<item>\n" + 
			"<title>RSS Solutions for Restaurants</title>\n" + 
			"<description>\n" + 
			"<b>FeedForAll </b>helps Restaurant's communicate with customers. Let your customers know the latest specials or events.<br/> <br/> RSS feed uses include:<br/> <font color=\"#FF0000\"><i>Daily Specials <br/> Entertainment <br/> Calendar of Events </i></font>\n" + 
			"</description>\n" + 
			"<link>http://www.feedforall.com/restaurant.htm</link>\n" + 
			"<category domain=\"www.dmoz.com\">\n" + 
			"Computers/Software/Internet/Site Management/Content Management\n" + 
			"</category>\n" + 
			"<comments>http://www.feedforall.com/forum</comments>\n" + 
			"<pubDate>Tue, 19 Oct 2004 11:09:11 -0400</pubDate>\n" + 
			"</item>\n" + 
			"<item>\n" + 
			"<title>RSS Solutions for Schools and Colleges</title>\n" + 
			"<description>\n" + 
			"FeedForAll helps Educational Institutions communicate with students about school wide activities, events, and schedules.<br/> <br/> RSS feed uses include:<br/> <i><font color=\"#0000FF\">Homework Assignments <br/> School Cancellations <br/> Calendar of Events <br/> Sports Scores <br/> Clubs/Organization Meetings <br/> Lunches Menus</font> </i>\n" + 
			"</description>\n" + 
			"<link>http://www.feedforall.com/schools.htm</link>\n" + 
			"<category domain=\"www.dmoz.com\">\n" + 
			"Computers/Software/Internet/Site Management/Content Management\n" + 
			"</category>\n" + 
			"<comments>http://www.feedforall.com/forum</comments>\n" + 
			"<pubDate>Tue, 19 Oct 2004 11:09:09 -0400</pubDate>\n" + 
			"</item>\n" + 
			"<item>\n" + 
			"<title>RSS Solutions for Computer Service Companies</title>\n" + 
			"<description>\n" + 
			"FeedForAll helps Computer Service Companies communicate with clients about cyber security and related issues. <br/> <br/> Uses include:<br/> <i><font color=\"#0000FF\">Cyber Security Alerts <br/> Specials<br/> Job Postings </font></i>\n" + 
			"</description>\n" + 
			"<link>http://www.feedforall.com/computer-service.htm</link>\n" + 
			"<category domain=\"www.dmoz.com\">\n" + 
			"Computers/Software/Internet/Site Management/Content Management\n" + 
			"</category>\n" + 
			"<comments>http://www.feedforall.com/forum</comments>\n" + 
			"<pubDate>Tue, 19 Oct 2004 11:09:07 -0400</pubDate>\n" + 
			"</item>\n" + 
			"<item>\n" + 
			"<title>RSS Solutions for Governments</title>\n" + 
			"<description>\n" + 
			"FeedForAll helps Governments communicate with the general public about positions on various issues, and keep the community aware of changes in important legislative issues. <i><br/> </i><br/> RSS uses Include:<br/> <i><font color=\"#00FF00\">Legislative Calendar<br/> Votes<br/> Bulletins</font></i>\n" + 
			"</description>\n" + 
			"<link>http://www.feedforall.com/government.htm</link>\n" + 
			"<category domain=\"www.dmoz.com\">\n" + 
			"Computers/Software/Internet/Site Management/Content Management\n" + 
			"</category>\n" + 
			"<comments>http://www.feedforall.com/forum</comments>\n" + 
			"<pubDate>Tue, 19 Oct 2004 11:09:05 -0400</pubDate>\n" + 
			"</item>\n" + 
			"<item>\n" + 
			"<title>RSS Solutions for Politicians</title>\n" + 
			"<description>\n" + 
			"FeedForAll helps Politicians communicate with the general public about positions on various issues, and keep the community notified of their schedule. <br/> <br/> Uses Include:<br/> <i><font color=\"#FF0000\">Blogs<br/> Speaking Engagements <br/> Statements<br/> </font></i>\n" + 
			"</description>\n" + 
			"<link>http://www.feedforall.com/politics.htm</link>\n" + 
			"<category domain=\"www.dmoz.com\">\n" + 
			"Computers/Software/Internet/Site Management/Content Management\n" + 
			"</category>\n" + 
			"<comments>http://www.feedforall.com/forum</comments>\n" + 
			"<pubDate>Tue, 19 Oct 2004 11:09:03 -0400</pubDate>\n" + 
			"</item>\n" + 
			"<item>\n" + 
			"<title>RSS Solutions for Meteorologists</title>\n" + 
			"<description>\n" + 
			"FeedForAll helps Meteorologists communicate with the general public about storm warnings and weather alerts, in specific regions. Using RSS meteorologists are able to quickly disseminate urgent and life threatening weather warnings. <br/> <br/> Uses Include:<br/> <i><font color=\"#0000FF\">Weather Alerts<br/> Plotting Storms<br/> School Cancellations </font></i>\n" + 
			"</description>\n" + 
			"<link>http://www.feedforall.com/weather.htm</link>\n" + 
			"<category domain=\"www.dmoz.com\">\n" + 
			"Computers/Software/Internet/Site Management/Content Management\n" + 
			"</category>\n" + 
			"<comments>http://www.feedforall.com/forum</comments>\n" + 
			"<pubDate>Tue, 19 Oct 2004 11:09:01 -0400</pubDate>\n" + 
			"</item>\n" + 
			"<item>\n" + 
			"<title>RSS Solutions for Realtors &amp; Real Estate Firms</title>\n" + 
			"<description>\n" + 
			"FeedForAll helps Realtors and Real Estate companies communicate with clients informing them of newly available properties, and open house announcements. RSS helps to reach a targeted audience and spread the word in an inexpensive, professional manner. <font color=\"#0000FF\"><br/> </font><br/> Feeds can be used for:<br/> <i><font color=\"#FF0000\">Open House Dates<br/> New Properties For Sale<br/> Mortgage Rates</font></i> \n" + 
			"</description>\n" + 
			"<link>http://www.feedforall.com/real-estate.htm</link>\n" + 
			"<category domain=\"www.dmoz.com\">\n" + 
			"Computers/Software/Internet/Site Management/Content Management\n" + 
			"</category>\n" + 
			"<comments>http://www.feedforall.com/forum</comments>\n" + 
			"<pubDate>Tue, 19 Oct 2004 11:08:59 -0400</pubDate>\n" + 
			"</item>\n" + 
			"<item>\n" + 
			"<title>RSS Solutions for Banks / Mortgage Companies</title>\n" + 
			"<description>\n" + 
			"FeedForAll helps <b>Banks, Credit Unions and Mortgage companies</b> communicate with the general public about rate changes in a prompt and professional manner. <br/> <br/> Uses include:<br/> <i><font color=\"#0000FF\">Mortgage Rates<br/> Foreign Exchange Rates <br/> Bank Rates<br/> Specials</font></i> \n" + 
			"</description>\n" + 
			"<link>http://www.feedforall.com/banks.htm</link>\n" + 
			"<category domain=\"www.dmoz.com\">\n" + 
			"Computers/Software/Internet/Site Management/Content Management\n" + 
			"</category>\n" + 
			"<comments>http://www.feedforall.com/forum</comments>\n" + 
			"<pubDate>Tue, 19 Oct 2004 11:08:57 -0400</pubDate>\n" + 
			"</item>\n" + 
			"<item>\n" + 
			"<title>RSS Solutions for Law Enforcement</title>\n" + 
			"<description>\n" + 
			"<b>FeedForAll</b> helps Law Enforcement Professionals communicate with the general public and other agencies in a prompt and efficient manner. Using RSS police are able to quickly disseminate urgent and life threatening information. <br/> <br/> Uses include:<br/> <i><font color=\"#0000FF\">Amber Alerts<br/> Sex Offender Community Notification <br/> Weather Alerts <br/> Scheduling <br/> Security Alerts <br/> Police Report <br/> Meetings</font></i> \n" + 
			"</description>\n" + 
			"<link>http://www.feedforall.com/law-enforcement.htm</link>\n" + 
			"<category domain=\"www.dmoz.com\">\n" + 
			"Computers/Software/Internet/Site Management/Content Management\n" + 
			"</category>\n" + 
			"<comments>http://www.feedforall.com/forum</comments>\n" + 
			"<pubDate>Tue, 19 Oct 2004 11:08:56 -0400</pubDate>\n" + 
			"</item>\n" + 
			"</channel>\n" + 
			"</rss>";
	
	public static final String ITUNES_RSS_XML = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
			"<rss xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\" version=\"2.0\">" + 
			"<channel>" + 
			"<title>Title of Podcast</title>" + 
			"<link>http://www.example.com/</link>" + 
			"<language>en-us</language>" + 
			"<itunes:subtitle>Subtitle of podcast</itunes:subtitle>" + 
			"<itunes:author>Author Name</itunes:author>" + 
			"<itunes:summary>Description of podcast.</itunes:summary>" + 
			"<description>Description of podcast.</description>" + 
			"<itunes:owner>" + 
			"    <itunes:name>Owner Name</itunes:name>" + 
			"    <itunes:email>me@example.com</itunes:email>" + 
			"</itunes:owner>" + 
			"<itunes:explicit>no</itunes:explicit>" + 
			"<itunes:image href=\"http://www.example.com/podcast-icon.jpg\" />" + 
			"<itunes:category text=\"Category Name\"></itunes:category>" + 
			"<item>" + 
			"    <title>Title of Podcast Episode</title>" + 
			"    <itunes:summary>Description of podcast episode content</itunes:summary>" + 
			"    <description>Description of podcast episode content</description>" + 
			"    <link>http://example.com/podcast-1</link>" + 
			"    <enclosure url=\"http://example.com/podcast-1/podcast.mp3\" type=\"audio/mpeg\" length=\"1024\"></enclosure>" + 
			"    <pubDate>Thu, 21 Dec 2016 16:01:07 +0000</pubDate>" + 
			"    <itunes:author>Author Name</itunes:author>" + 
			"    <itunes:duration>00:32:16</itunes:duration>" + 
			"    <itunes:explicit>no</itunes:explicit>" + 
			"    <guid>http://example.com/podcast-1</guid>" + 
			"</item> " +  
			"</channel>" + 
			"</rss>";
	
	@Test
	public void testITunes() throws MalformedFeedException, MalformedURLException, DateFormatException {
		Podcast p = new Podcast(ITUNES_RSS_XML);
		String[] categories = p.getCategories();
		for(String c : categories) {
			assertNotNull(c);
		}
	}

	private static Podcast podcast;

    @BeforeAll
    public static void setup() throws Exception {
    	String source = Files.readString(
    		    Paths.get(PodcastTest.class.getResource("/testfeed.rss").toURI()), Charset.defaultCharset());
        podcast = new Podcast(source);
    }

    @Test
    public void testOverview() throws MalformedFeedException, MalformedURLException, DateFormatException {
        assertEquals("Testing Feed", podcast.getTitle());
        assertEquals("A dummy podcast feed for testing the Podcast Feed Library.", podcast.getDescription());
        assertEquals("https://podcast-feed-library.owl.im/feed", podcast.getLink().toString());
        assertEquals("en-GB", podcast.getLanguage());
        assertEquals("Copyright © 2017 Icosillion", podcast.getCopyright());
        assertEquals("Marcus Lewis (marcus@icosillion.com)", podcast.getManagingEditor());
        assertEquals("Marcus Lewis (marcus@icosillion.com)", podcast.getWebMaster());
        assertEquals("Mon, 12 Dec 2016 15:30:00 GMT", podcast.getPubDateString());
        assertEquals(DateUtils.stringToDate("Mon, 12 Dec 2016 15:30:00 GMT"), podcast.getPubDate());
        assertEquals(DateUtils.stringToDate("Mon, 12 Dec 2016 15:30:00 GMT"), podcast.getLastBuildDate());
        assertEquals("Mon, 12 Dec 2016 15:30:00 GMT", podcast.getLastBuildDateString());
        assertArrayEquals(new String[] { "Technology" }, podcast.getCategories());
        assertEquals("Handcrafted", podcast.getGenerator());
        assertEquals("https://podcast-feed-library.owl.im/docs", podcast.getDocs().toString());
        assertEquals(60, (int) podcast.getTTL());
        assertEquals("https://podcast-feed-library.owl.im/images/artwork.png", podcast.getImageURL().toString());
        assertNull(podcast.getPICSRating());

        Set<Integer> skipHours = podcast.getSkipHours();
        assertTrue(skipHours.contains(0));
        assertTrue(skipHours.contains(4));
        assertTrue(skipHours.contains(8));
        assertTrue(skipHours.contains(12));
        assertTrue(skipHours.contains(16));

        Set<String> skipDays = podcast.getSkipDays();
        assertTrue(skipDays.contains("Monday"));
        assertTrue(skipDays.contains("Wednesday"));
        assertTrue(skipDays.contains("Friday"));
        assertArrayEquals(new String[] { "podcast", "java", "xml", "dom4j", "icosillion", "maven" } , podcast.getKeywords());
        assertEquals(1, podcast.getEpisodes().size());
    }

    @Test
    public void testTextInput() throws MalformedURLException {
        TextInputInfo textInput = podcast.getTextInput();
        assertEquals("Feedback", textInput.getTitle());
        assertEquals("Feedback for the Testing Feed", textInput.getDescription());
        assertEquals("feedback", textInput.getName());
        assertEquals("https://podcast-feed-library.owl.im/feedback/submit", textInput.getLink().toString());
    }

    @Test
    public void testITunesInfo() throws Exception {
        ITunesChannelInfo iTunesInfo = podcast.getITunesInfo();
        assertEquals("Icosillion", iTunesInfo.getAuthor());
        assertEquals("A dummy podcast feed for testing the Podcast Feed Library.", iTunesInfo.getSubtitle());
        assertEquals("This podcast brings testing capabilities to the Podcast Feed Library", iTunesInfo.getSummary());
        assertEquals(false, iTunesInfo.isBlocked());
        assertEquals(ITunesInfo.ExplicitLevel.CLEAN, iTunesInfo.getExplicit());
        assertEquals("https://podcast-feed-library.owl.im/images/artwork.png", iTunesInfo.getImage().toString());
        assertEquals(ITunesChannelInfo.FeedType.SERIAL, iTunesInfo.getType());
    }

    @Test
    public void testITunesOwnerInfo() {
        ITunesOwner iTunesOwner = podcast.getITunesInfo().getOwner();
        assertEquals("Icosillion", iTunesOwner.getName());
        assertEquals("hello@icosillion.com", iTunesOwner.getEmail());
    }

    @Test
    public void testCloudInfo() {
        CloudInfo cloudInfo = podcast.getCloud();
        assertEquals("rpc.owl.im", cloudInfo.getDomain());
        assertEquals(8080, (int) cloudInfo.getPort());
        assertEquals("/rpc", cloudInfo.getPath());
        assertEquals("owl.register", cloudInfo.getRegisterProcedure());
        assertEquals("xml-rpc", cloudInfo.getProtocol());
    }
}
