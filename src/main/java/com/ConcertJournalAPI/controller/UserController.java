package com.ConcertJournalAPI.controller;

import com.ConcertJournalAPI.model.AppUser;
import com.ConcertJournalAPI.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody AppUser user) {
        // Check if username already exists
        if (appUserRepository.findByEmail(user.getEmail()) != null) {
            return "User already exists";
        }
        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        appUserRepository.save(user);
        //set role to user by default
        user.setRole("USER");
        return "User registered successfully";
    }
}
