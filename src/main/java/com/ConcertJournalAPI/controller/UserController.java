package com.ConcertJournalAPI.controller;

import com.ConcertJournalAPI.model.AppUser;
import com.ConcertJournalAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerUser(@RequestBody AppUser user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "Username already exists";
        }
        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully";
    }
}
