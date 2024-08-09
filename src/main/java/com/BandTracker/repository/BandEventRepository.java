package com.BandTracker.repository;

import com.BandTracker.model.BandEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BandEventRepository extends JpaRepository<BandEvent, Long> {
}
