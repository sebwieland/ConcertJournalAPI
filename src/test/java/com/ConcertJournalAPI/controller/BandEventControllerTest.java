package com.ConcertJournalAPI.controller;

import com.ConcertJournalAPI.model.AppUser;
import com.ConcertJournalAPI.model.BandEvent;
import com.ConcertJournalAPI.repository.UserRepository;
import com.ConcertJournalAPI.service.BandEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BandEventControllerTest {

    public static final String TEST_USERNAME = "testUsername";
    @Mock
    private BandEventService bandEventService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppUser appUser1;

    @Mock
    private AppUser appUser2;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private BandEventController bandEventController;

    private BandEvent bandEvent1;
    private BandEvent bandEvent2;
    private BandEvent bandEvent3;

    @BeforeEach
    void setup() {

        bandEvent1 = new BandEvent();
        bandEvent1.setBandName("TestBand1");
        bandEvent1.setPlace("TestPlace1");
        bandEvent1.setDate(LocalDate.now());
        bandEvent1.setAppUser(appUser1);

        bandEvent2 = new BandEvent();
        bandEvent2.setBandName("TestBand2");
        bandEvent2.setPlace("TestPlace2");
        bandEvent2.setDate(LocalDate.now());
        bandEvent2.setAppUser(appUser1);

        bandEvent3 = new BandEvent();
        bandEvent3.setBandName("TestBand3");
        bandEvent3.setPlace("TestPlace3");
        bandEvent3.setDate(LocalDate.now());
        bandEvent3.setAppUser(appUser2);

    }

    @Test
    void testGetAllEvents() {
        List<BandEvent> events = Arrays.asList(bandEvent1, bandEvent2);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(appUser1);
        when(bandEventService.getAllEventsForCurrentUser(appUser1)).thenReturn(events);

        List<BandEvent> response = bandEventController.getAllEvents(TEST_USERNAME);
        assertEquals(events, response);
    }

    @Test
    void testGetEventById() {
        when(bandEventService.getEventById(1L)).thenReturn(bandEvent1);

        BandEvent response = bandEventController.getEventById(1L);

        assertEquals(bandEvent1, response);
        verify(bandEventService).getEventById(1L);
    }

    @Test
    void testCreateEvent() {
        when(bandEventService.saveEvent(bandEvent1)).thenReturn(bandEvent1);
        when(userRepository.findByUsername(anyString())).thenReturn(appUser1);

        BandEvent response = bandEventController.createEvent(bandEvent1, TEST_USERNAME);

        assertEquals(bandEvent1, response);
        verify(bandEventService).saveEvent(bandEvent1);
    }

    @Test
    void testDeleteEvent() {
        bandEventController.deleteEvent(1L);

        verify(bandEventService).deleteEventById(1L);
    }
}