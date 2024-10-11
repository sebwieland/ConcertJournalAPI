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

    @Mock
    private BandEventService bandEventService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppUser appUser;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private BandEventController bandEventController;

    private BandEvent bandEvent1;
    private BandEvent bandEvent2;

    @BeforeEach
    void setup() {


        bandEvent1 = new BandEvent();
        bandEvent1.setBandName("TestBand1");
        bandEvent1.setPlace("TestPlace1");
        bandEvent1.setDate(LocalDate.now());
        bandEvent1.setAppUser(appUser);

        bandEvent2 = new BandEvent();
        bandEvent2.setBandName("TestBand1");
        bandEvent2.setPlace("TestPlace1");
        bandEvent2.setDate(LocalDate.now());
        bandEvent2.setAppUser(appUser);


    }

    @Test
    void testGetAllEvents() {
        List<BandEvent> events = Arrays.asList(bandEvent1, bandEvent2);
        when(bandEventService.getAllEventsForCurrentUser(appUser)).thenReturn(events);
        when(userDetails.getUsername()).thenReturn("TestUser");
        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(appUser);

        List<BandEvent> response = bandEventController.getAllEvents(userDetails);

        assertEquals(events, response);
        verify(bandEventService).getAllEventsForCurrentUser(appUser);
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
        when(userRepository.findByUsername(anyString())).thenReturn(new AppUser());
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUsername");

        BandEvent response = bandEventController.createEvent(bandEvent1, userDetails);

        assertEquals(bandEvent1, response);
        verify(bandEventService).saveEvent(bandEvent1);
    }

    @Test
    void testDeleteEvent() {
        bandEventController.deleteEvent(1L);

        verify(bandEventService).deleteEventById(1L);
    }
}