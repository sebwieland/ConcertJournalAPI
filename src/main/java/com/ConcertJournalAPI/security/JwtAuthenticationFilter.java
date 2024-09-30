package com.ConcertJournalAPI.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import static com.ConcertJournalAPI.configuration.SecurityConstants.AUTHORIZATION_HEADER;
import static com.ConcertJournalAPI.configuration.SecurityConstants.BEARER_PREFIX;
import static com.ConcertJournalAPI.security.JwtUtils.getSigningKey;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final String jwtSecret;

    public JwtAuthenticationFilter(@NonNull String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromRequest(request);
        if (token != null) {
            try {
                Claims claims = parseToken(token);
                authenticateUser(claims);
            } catch (JwtException e) {
                handleInvalidToken(response);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private Claims parseToken(String token) throws JwtException {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey(jwtSecret))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        if (claims.getExpiration().before(new Date())) {
            throw new JwtException("Token has expired");
        }
        return claims;
    }

    private void authenticateUser(Claims claims) {
        String username = claims.getSubject();
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleInvalidToken(HttpServletResponse response) {
        logger.error("Invalid token: {}");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        try {
            response.getWriter().write("{\"error\":\"Invalid token\"}");
        } catch (IOException ex) {
            logger.error("Error writing response: {}");
        }
    }
}

