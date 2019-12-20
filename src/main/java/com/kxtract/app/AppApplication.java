package com.kxtract.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.kxtract.data.PodcastRepository;
import com.kxtract.data.dao.Podcast;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "com.kxtract.data" })
@EntityScan("com.kxtract.data.dao*")
public class AppApplication {
	private static final Logger log = LoggerFactory.getLogger(AppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(PodcastRepository repository) {
		return (args) -> {

			// fetch all 
			log.info("Podcast found with findAll():");
			log.info("-------------------------------");
			for (Podcast p : repository.findAll()) {
				log.info(p.toString());
			}
			log.info("");

			repository.save(new Podcast("Test", "Rss1"));
			repository.save(new Podcast("Test", "Rss2"));
		
			
			// fetch an individual podcast by ID
			Podcast p = repository.findById(1);
			log.info("Podcast found with findById(1):");
			log.info("--------------------------------");
			log.info(p.toString());
			log.info("");
		};
	}
}
