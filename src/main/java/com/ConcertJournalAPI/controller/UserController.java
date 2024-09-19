package com.ConcertJournalAPI.controller;

import com.ConcertJournalAPI.model.AppUser;
import com.ConcertJournalAPI.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody AppUser user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "Username already exists";
        }
        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        //set role to user by default
        user.setRole("USER");
        return "User registered successfully";
    }
}
