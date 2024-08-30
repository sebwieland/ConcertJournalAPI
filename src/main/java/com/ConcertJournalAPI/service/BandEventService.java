package com.ConcertJournalAPI.service;

import com.ConcertJournalAPI.model.BandEvent;
import com.ConcertJournalAPI.repository.BandEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BandEventService {
    @Autowired
    private BandEventRepository bandEventRepository;

    public List<BandEvent> getAllEvents() {
        return bandEventRepository.findAll();
    }

    public BandEvent getEventById(Long id) {
        return bandEventRepository.findById(id).orElse(null);
    }

    public BandEvent saveEvent(BandEvent bandEvent) {
        return bandEventRepository.save(bandEvent);
    }

    public void deleteEventById(Long id) {
        bandEventRepository.deleteById(id);
    }

}
