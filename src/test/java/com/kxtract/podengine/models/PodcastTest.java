package com.kxtract.podengine.models;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.kxtract.podengine.exceptions.MalformedFeedException;

public class PodcastTest {

	@Test
	public void testCtor_nullXML() throws MalformedFeedException {
		String xml = null;
		
		Throwable exception = assertThrows(IllegalArgumentException.class,
	            ()->{Podcast p = new Podcast(xml);} );
	}
	
	@Test
	public void testCtor_blankStringXML() throws MalformedFeedException {
		String xml = "";
		
		Throwable exception = assertThrows(MalformedFeedException.class,
	            ()->{Podcast p = new Podcast(xml);} );
	}

	
	@Test
	public void testCtor_SampleXML() throws MalformedFeedException {
		Podcast p = new Podcast(EXAMPLE_RSS_XML_SIMPLE);
	}
	
	@Test
	public void testCtor_SampleXML_Complex() throws MalformedFeedException {
		Podcast p = new Podcast(EXAMPLE_RSS_XML_COMPLEX);
	}
	
	private static final String EXAMPLE_RSS_XML_SIMPLE = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + 
			"<rss version=\"2.0\">\n" + 
			"\n" + 
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
			"\n" + 
			"</rss>";
	
	
	private static final String EXAMPLE_RSS_XML_COMPLEX = "<rss version=\"2.0\">\n" + 
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
}
