package com.ConcertJournalAPI.controller;

import com.ConcertJournalAPI.model.AppUser;
import com.ConcertJournalAPI.model.BandEvent;
import com.ConcertJournalAPI.repository.UserRepository;
import com.ConcertJournalAPI.service.BandEventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@Validated
@RestController
public class BandEventController {
    @Autowired
    private BandEventService bandEventService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/AllEvents")
    public List<BandEvent> getAllEvents() {
        return bandEventService.getAllEvents();
    }

    @GetMapping("/event/{id}")
    public BandEvent getEventById(@PathVariable Long id) {
        return bandEventService.getEventById(id);
    }

    @PostMapping("/event")
    public BandEvent createEvent(@RequestBody @Valid BandEvent bandEvent, @AuthenticationPrincipal UserDetails userDetails) {
        AppUser appUser = userRepository.findByUsername(userDetails.getUsername());
        bandEvent.setAppUser(appUser);
        return bandEventService.saveEvent(bandEvent);
    }

    @DeleteMapping("event/{id}")
    public void deleteEvent(@PathVariable Long id) {
        bandEventService.deleteEventById(id);
    }
}
