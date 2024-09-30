package com.ConcertJournalAPI.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;

import static com.ConcertJournalAPI.configuration.SecurityConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    private final String jwtSecret = "QXUH21ujrFnMDl4IT90PzBeTI21E5z4soM+gbJu7hm8=\"";

    private static final String TEST_TOKEN = "Bearer test-token";

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setup() {
    }

    @Test
    void testGetSigningKey() {
        SecretKey signingKey = JwtUtils.getSigningKey(jwtSecret);
        assertNotNull(signingKey);
    }

    @Test
    void testExtractTokenFromRequest() {
        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(TEST_TOKEN);
        String token = JwtUtils.extractTokenFromRequest(request);
        assertEquals("test-token", token);
    }

    @Test
    void testExtractTokenFromRequestNoHeader() {
        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(null);
        String token = JwtUtils.extractTokenFromRequest(request);
        assertNull(token);
    }

    @Test
    void testExtractTokenFromRequestInvalidHeader() {
        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn("Invalid token");
        String token = JwtUtils.extractTokenFromRequest(request);
        assertNull(token);
    }

    @Test
    void testParseToken() throws JwtException {
        String token = Jwts.builder()
                .setSubject("test-subject")
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
        Claims claims = JwtUtils.parseToken(token, jwtSecret);
        assertNotNull(claims);
        assertEquals("test-subject", claims.getSubject());
    }

    @Test
    void testParseTokenExpired() {
        String token = Jwts.builder()
                .setSubject("test-subject")
                .setExpiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
        assertThrows(JwtException.class, () -> JwtUtils.parseToken(token, jwtSecret));
    }

    @Test
    void testParseTokenInvalid() {
        String token = "Invalid token";
        assertThrows(JwtException.class, () -> JwtUtils.parseToken(token, jwtSecret));
    }
}