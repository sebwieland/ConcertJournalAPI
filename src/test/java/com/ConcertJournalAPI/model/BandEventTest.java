package com.ConcertJournalAPI.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class BandEventTest {

    private Validator validator;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void setupValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create validator", e);
        }
    }

    @Test
    void testDefaultConstructor() {
        BandEvent bandEvent = new BandEvent();
        assertNotNull(bandEvent);
    }

    @Test
    void testGettersAndSetters() {
        BandEvent bandEvent = new BandEvent();
        bandEvent.setId(1L);
        bandEvent.setBandName("Test Band");
        bandEvent.setPlace("Test Place");
        bandEvent.setDate(LocalDate.now());

        assertEquals(1L, bandEvent.getId());
        assertEquals("Test Band", bandEvent.getBandName());
        assertEquals("Test Place", bandEvent.getPlace());
        assertEquals(LocalDate.now(), bandEvent.getDate());
    }

    @Test
    void testValidation_BandNameNotBlank() {

        BandEvent bandEvent = new BandEvent();
        bandEvent.setBandName("");
        bandEvent.setDate(LocalDate.now());
        Set<ConstraintViolation<BandEvent>> violations = validator.validate(bandEvent);

        assertEquals(1, violations.size());
        assertEquals("Band Name is required.", violations.iterator().next().getMessage());
    }

    @Test
    void testValidation_DateNotBlank() {
        BandEvent bandEvent = new BandEvent();
        bandEvent.setBandName("Test Band");
        Set<ConstraintViolation<BandEvent>> violations = validator.validate(bandEvent);

        assertEquals(1, violations.size());
        assertEquals("Date is required.", violations.iterator().next().getMessage());
    }

    @Test
    void testToString() {
        BandEvent bandEvent = new BandEvent();
        bandEvent.setId(1L);
        bandEvent.setBandName("Test Band");
        bandEvent.setPlace("Test Place");
        bandEvent.setDate(LocalDate.now());

        String expected = "BandEvent(id=1, bandName=Test Band, place=Test Place, date=" + LocalDate.now() + ")";
        assertEquals(expected, bandEvent.toString());
    }

    @Test
    public void testValidBandEventCreation() {
        BandEvent bandEvent = new BandEvent();
        bandEvent.setBandName("Test Band");
        bandEvent.setPlace("Test Place");
        bandEvent.setDate(LocalDate.now());

        assertNotNull(bandEvent);
        assertEquals("Test Band", bandEvent.getBandName());
        assertEquals("Test Place", bandEvent.getPlace());
        assertNotNull(bandEvent.getDate());
    }

    @Test
    public void testInvalidBandEventCreation_BandNameIsNull() {
        BandEvent bandEvent = new BandEvent();
        bandEvent.setPlace("Test Place");
        bandEvent.setDate(LocalDate.now());

        assertThrows(NullPointerException.class, () -> {
            entityManager.persist(bandEvent);
            entityManager.flush();
        });
    }

    @Test
    public void testInvalidBandEventCreation_DateIsNull() {
        BandEvent bandEvent = new BandEvent();
        bandEvent.setBandName("Test Band");
        bandEvent.setPlace("Test Place");

        assertThrows(NullPointerException.class, () -> {
            entityManager.persist(bandEvent);
            entityManager.flush();
        });
    }

}