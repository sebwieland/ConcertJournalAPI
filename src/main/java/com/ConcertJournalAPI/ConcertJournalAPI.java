package com.ConcertJournalAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication
@EnableJpaAuditing
@EnableWebMvc
public class ConcertJournalAPI {
	public static void main(String[] args) {
		SpringApplication.run(ConcertJournalAPI.class, args);
	}
}
