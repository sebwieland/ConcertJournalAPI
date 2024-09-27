package com.ConcertJournalAPI.repository;

import com.ConcertJournalAPI.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    AppUser findByEmail(String email);
    Boolean existsAppUserByUsername(String username);
    Boolean existsAppUserByEmail(String email);
}
