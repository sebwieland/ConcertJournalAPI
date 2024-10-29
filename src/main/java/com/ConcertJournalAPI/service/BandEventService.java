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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("User is not authenticated");
        }
        String username = authentication.getName();
        AppUser appUser = userRepository.findByUsername(username);
        return bandEventRepository.findAllByAppUser(appUser);
    }

    public BandEvent getEventById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("User is not authenticated");
        }
        String username = authentication.getName();
        AppUser appUser = userRepository.findByUsername(username);
        return bandEventRepository.findByIdAndAppUser(id,appUser).orElse(null);
    }

    public void deleteEventById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("User is not authenticated");
        }
        String username = authentication.getName();
        AppUser appUser = userRepository.findByUsername(username);
        bandEventRepository.deleteByIdAndAppUser(id, appUser);
    }

    public BandEvent saveEvent(BandEvent bandEvent) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("User is not authenticated");
        }
        String username = authentication.getName();
        AppUser appUser = userRepository.findByUsername(username);
        bandEvent.setAppUser(appUser);
        return bandEventRepository.save(bandEvent);
    }

}
