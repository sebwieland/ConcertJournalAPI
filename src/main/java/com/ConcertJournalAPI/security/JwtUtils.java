package com.ConcertJournalAPI.security;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

class JwtUtils {

    static SecretKey getSigningKey(String jwtSecret) {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}