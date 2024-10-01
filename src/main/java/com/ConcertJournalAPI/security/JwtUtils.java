package com.ConcertJournalAPI.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.SecretKey;
import java.util.Date;

import static com.ConcertJournalAPI.configuration.SecurityConstants.*;

class JwtUtils {

    static SecretKey getSigningKey(String jwtSecret) {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public static String extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public static Claims parseToken(String token, String secretKey) throws JwtException {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        if (claims == null) {
            throw new JwtException("Invalid token");
        }
        if (claims.getExpiration().before(new Date())) {
            throw new JwtException("Token has expired");
        }
        if (claims.getExpiration() == null) {
            throw new JwtException("Token has no expiration date");
        }
        return claims;
    }

}