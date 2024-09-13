package com.ConcertJournalAPI;

import com.ConcertJournalAPI.model.AppUser;
import com.ConcertJournalAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if users already exist
        if (userRepository.count() == 0) {
            // Create default user
            AppUser user = new AppUser();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("adminpassword"));
            userRepository.save(user);

            System.out.println("Default user created with username 'admin' and password 'adminpassword'");
        }
    }
}
