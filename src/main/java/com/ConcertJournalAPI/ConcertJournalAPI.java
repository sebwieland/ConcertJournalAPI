package com.ConcertJournalAPI;

import com.ConcertJournalAPI.model.BandEvent;
import com.ConcertJournalAPI.repository.BandEventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDate;
import java.util.stream.IntStream;

@SpringBootApplication
@EnableWebMvc
public class ConcertJournalAPI {

	@Bean
	public CommandLineRunner initData(BandEventRepository repository) {
		return (args) -> {
			IntStream.range(0, 20) // replace N with the desired number of dummy events
					.forEach(i -> repository.save(createDummyBandEvent(i)));
		};
	}

	private BandEvent createDummyBandEvent(int index) {
		BandEvent dummyEvent = new BandEvent();
		dummyEvent.setBandName("Bandname" + (index + 1));
		dummyEvent.setPlace("here" + (index + 1));
		dummyEvent.setDate(LocalDate.now());
		return dummyEvent;
	}

	public static void main(String[] args) {
		SpringApplication.run(ConcertJournalAPI.class, args);
	}
}
