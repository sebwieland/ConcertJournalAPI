package com.ConcertJournalAPI.service;

import com.ConcertJournalAPI.model.AppUser;
import com.ConcertJournalAPI.model.BandEvent;
import com.ConcertJournalAPI.repository.BandEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BandEventServiceTest {

    @Mock
    private BandEventRepository bandEventRepository;

    @InjectMocks
    private BandEventService bandEventService;

    @BeforeEach
    public void setup() {
        //MockitoAnnotations.initMocks(this);
    }

    public BandEvent getSampleBandEvent() {
        BandEvent sampleEvent = new BandEvent();
        Long randomId = (long) (Math.random() * 1000);
        sampleEvent.setId(randomId);
        sampleEvent.setAppUser(new AppUser());
        return sampleEvent;
    }

    @Test
    void testGetAllEvents() {
        // Given: a mock repository with some sample BandEvents
        List<BandEvent> sampleEvents = Arrays.asList(getSampleBandEvent(), getSampleBandEvent());
        when(bandEventRepository.findAll()).thenReturn(sampleEvents);

        // When: calling the service method
        List<BandEvent> result = bandEventService.getAllEvents();

        // Then: verify the result
        assertNotNull(result);
        assertEquals(sampleEvents, result);
    }

    @Test
    void testGetEventById() {
        // Given: a mock repository with one sample BandEvent
        BandEvent sampleEvent = getSampleBandEvent();
        when(bandEventRepository.findById(sampleEvent.getId())).thenReturn(java.util.Optional.of(sampleEvent));

        // When: calling the service method
        BandEvent result = bandEventService.getEventById(sampleEvent.getId());

        // Then: verify the result
        assertNotNull(result);
        assertEquals(sampleEvent, result);
    }

    @Test
    void testSaveEvent() {
        // Given: a mock repository with one sample BandEvent
        BandEvent sampleEvent = getSampleBandEvent();
        when(bandEventRepository.save(sampleEvent)).thenReturn(sampleEvent);

        // When: calling the service method
        BandEvent result = bandEventService.saveEvent(sampleEvent);

        // Then: verify the result
        assertNotNull(result);
        assertEquals(sampleEvent, result);
    }

    @Test
    void testDeleteEventById() {
        // Given: a mock repository with one sample BandEvent
        BandEvent sampleEvent = getSampleBandEvent();
        //bandEventRepository.deleteById(sampleEvent.getId());

        // When: calling the service method
        bandEventService.deleteEventById(sampleEvent.getId());

        // Then: verify the result
        verify(bandEventRepository, times(1)).deleteById(sampleEvent.getId());
    }


}