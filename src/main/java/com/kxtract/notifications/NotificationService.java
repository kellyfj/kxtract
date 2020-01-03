package com.kxtract.notifications;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kxtract.data.SubscriptionRepository;
import com.kxtract.data.dao.Podcast;
import com.kxtract.data.dao.Subscription;

@Component
public class NotificationService {
	private Logger logger = LoggerFactory.getLogger(NotificationService.class);
	
	@Autowired
	private EventChecker eventChecker;
	
	@Autowired
	private SubscriptionRepository subsRepo;
	
	public void performScanAndNotificationTransmission() {
		List<Podcast> podcastsWithNewEpisodes = eventChecker.checkForPodcastsWithNewEpisodes();
		if (podcastsWithNewEpisodes.isEmpty()) {
			logger.info("No new Episodes found!");
		}

		for(Podcast p : podcastsWithNewEpisodes) {
			
			//Get all users for each podcast
			List<Subscription> subscriptions = subsRepo.findByPodcastId(p.getId());
			
			if(subscriptions.isEmpty()) {
				logger.info("No Subscriptions for Podcast " + p.getName());
			}
			//Send email
			for(Subscription s : subscriptions) {
				logger.info("Sending email to user "+ s.getUserId() + " for Podcast " + p.getName());
				
				String emailSubject = "Podcast [" + p.getName() + "] has a new episode!";
				String textBody = "Your podcast subscription " + p.getName() + " has a new episode";
				
				Emailer.sendNotificationEmail(s.getUserId(), emailSubject, textBody);
			}
		}
	}
}
