package com.ConcertJournalAPI;

import com.ConcertJournalAPI.model.AppUser;
import com.ConcertJournalAPI.model.BandEvent;
import com.ConcertJournalAPI.repository.BandEventRepository;
import com.ConcertJournalAPI.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.stream.IntStream;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      BandEventRepository bandEventRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
//         userRepository.delete(userRepository.findByEmail("admin@example.com"));
        // Check if users already exist
        if (!userRepository.existsAppUserByEmail("admin@example.com")) {
            // Create default user
            AppUser user = new AppUser();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole("ADMIN");
            user.setEmail("admin@example.com");
            userRepository.save(user);

            LOGGER.info("Default admin user created with username 'admin@example.com' and password 'password'");
        } else {
            LOGGER.info("Default admin user already exists with username 'admin@example.com' and password 'password'");
        }

        // IntStream.range(0, 10) // replace N with the desired number of dummy events
        //        .forEach(i -> bandEventRepository.save(createDummyBandEvent(i)));
    }


    private BandEvent createDummyBandEvent(int index) {
        BandEvent dummyEvent = new BandEvent();
        dummyEvent.setBandName("Bandname" + (index + 1));
        dummyEvent.setPlace("here" + (index + 1));
        dummyEvent.setDate(LocalDate.now());
        dummyEvent.setRating(0);
        dummyEvent.setComment("comment" + (index + 1));
        dummyEvent.setCreationDate(Instant.now());
        dummyEvent.setModificationDate(Instant.now());
        return dummyEvent;
    }


}
