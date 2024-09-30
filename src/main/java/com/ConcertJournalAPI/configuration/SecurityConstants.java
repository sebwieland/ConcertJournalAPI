package com.ConcertJournalAPI.configuration;

import org.springframework.beans.factory.annotation.Value;

public class SecurityConstants {
    @Value("${jwt.secret}")
    public static String JWT_SECRET;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
}