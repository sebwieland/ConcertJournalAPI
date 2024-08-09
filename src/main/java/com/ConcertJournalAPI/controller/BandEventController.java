package com.ConcertJournalAPI.controller;

import com.ConcertJournalAPI.model.BandEvent;
import com.ConcertJournalAPI.service.BandEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/BandTracker/events")
public class BandEventController {
    @Autowired
    private BandEventService bandEventService;

    @GetMapping
    public List<BandEvent> getAllEvents() {
        return bandEventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public BandEvent getEventById(@PathVariable Long id) {
        return bandEventService.getEventById(id);
    }

    @PostMapping
    public BandEvent createEvent(@RequestBody BandEvent bandEvent) {
        return bandEventService.saveEvent(bandEvent);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        bandEventService.deleteEventById(id);
    }
}
