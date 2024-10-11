package com.ConcertJournalAPI.repository;

import com.ConcertJournalAPI.model.AppUser;
import com.ConcertJournalAPI.model.BandEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BandEventRepository extends JpaRepository<BandEvent, Long> {
    List<BandEvent> findAllByAppUser(AppUser appUser);
}
