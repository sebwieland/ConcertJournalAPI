package com.ConcertJournalAPI.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
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
    void testParseTokenValidToken() throws JwtException {
        String token = generateToken(jwtSecret, "test-subject", 10000);
        Claims claims = JwtUtils.parseToken(token, jwtSecret);
        assertNotNull(claims);
        assertEquals("test-subject", claims.getSubject());
    }

    @Test
    void testParseTokenExpiredToken() {
        String token = generateToken(jwtSecret, "test-subject", -1);
        assertThrows(JwtException.class, () -> JwtUtils.parseToken(token, jwtSecret));
    }

    @Test
    void testParseTokenInvalid() {
        String token = "Invalid token";
        assertThrows(JwtException.class, () -> JwtUtils.parseToken(token, jwtSecret));
    }

    @Test
    public void testParseTokenNullClaims() {
        String token = Jwts.builder()
                .signWith(JwtUtils.getSigningKey(jwtSecret), SignatureAlgorithm.HS256)
                .compact();
        assertThrows(JwtException.class, () -> JwtUtils.parseToken(token, jwtSecret));
    }

    private String generateToken(String secretKey, String subject, long expirationTime) {
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expirationDate)
                .signWith(JwtUtils.getSigningKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }


}