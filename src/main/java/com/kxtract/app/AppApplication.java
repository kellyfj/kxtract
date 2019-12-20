package com.kxtract.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.kxtract.data.EpisodeRepository;
import com.kxtract.data.PodcastRepository;
import com.kxtract.data.SubscriptionRepository;
import com.kxtract.data.dao.Episode;
import com.kxtract.data.dao.Podcast;
import com.kxtract.data.dao.Subscription;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "com.kxtract.data" })
@EntityScan("com.kxtract.data.dao*")
public class AppApplication {
	private static final Logger log = LoggerFactory.getLogger(AppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(PodcastRepository podRepo, EpisodeRepository episodeRepo, SubscriptionRepository subsRepo) {
		return (args) -> {

			// fetch all 
			log.info("Podcast found with findAll():");
			log.info("-------------------------------");
			for (Podcast p : podRepo.findAll()) {
				log.info(p.toString());
			}
			log.info("");

			podRepo.save(new Podcast("Test", "Rss1"));
			podRepo.save(new Podcast("Test", "Rss2"));		
			
			// fetch an individual podcast by ID
			Podcast p = podRepo.findById(1);
			log.info("Podcast found with findById(1):");
			log.info("--------------------------------");
			log.info(p.toString());
			log.info("");
			
			List<Podcast> deleteTestPodcasts = podRepo.findByName("Test");
			for(Podcast pod: deleteTestPodcasts) {
				log.info("Deleteing Podcast " + pod.getId() + " . . . . ");
				podRepo.delete(pod);
			}
			log.info("");
			
			//Episodes
			log.info("Episodes found with findAll():");
			log.info("-------------------------------");
			for (Episode e : episodeRepo.findAll()) {
				log.info(e.toString());
			}
			log.info("");

		
			//Subscriptions
			log.info("Subscriptions found with findAll():");
			log.info("-------------------------------");
			for (Subscription s : subsRepo.findAll()) {
				log.info(s.toString());
			}
			log.info("");
		};
	}
}
