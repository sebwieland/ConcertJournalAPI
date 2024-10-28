package com.ConcertJournalAPI.service;

import com.ConcertJournalAPI.model.AppUser;
import com.ConcertJournalAPI.model.BandEvent;
import com.ConcertJournalAPI.repository.BandEventRepository;
import com.ConcertJournalAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BandEventService {
    @Autowired
    private BandEventRepository bandEventRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BandEvent> getAllEvents() {
        return bandEventRepository.findAll();
    }

    public List<BandEvent> getAllEventsForCurrentUser(AppUser user) {
        return bandEventRepository.findAllByAppUser(user);
    }

    public BandEvent getEventById(Long id) {
        return bandEventRepository.findById(id).orElse(null);
    }

    public BandEvent saveEvent(BandEvent bandEvent) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        AppUser appUser = userRepository.findByUsername(username);
        bandEvent.setAppUser(appUser);
        return bandEventRepository.save(bandEvent);
    }

    public void deleteEventById(Long id) {
        bandEventRepository.deleteById(id);
    }

}
