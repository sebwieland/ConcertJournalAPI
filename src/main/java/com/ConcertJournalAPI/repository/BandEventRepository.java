package com.ConcertJournalAPI.repository;

import com.ConcertJournalAPI.model.BandEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BandEventRepository extends JpaRepository<BandEvent, Long> {
}
