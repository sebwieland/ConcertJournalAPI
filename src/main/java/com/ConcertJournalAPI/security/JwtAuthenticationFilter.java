package com.ConcertJournalAPI.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public JwtAuthenticationFilter(AuthSuccessHandler authSuccessHandler) {
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }
        if (accessToken != null) {
            try {
                Claims claims = JwtUtils.parseToken(accessToken);
                authenticateUser(claims);
            } catch (JwtException e) {
                // Handle invalid token
            }
        }
        filterChain.doFilter(request, response);
    }

    void authenticateUser(Claims claims) {
        String username = claims.getSubject();
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    void handleInvalidToken(HttpServletResponse response) {
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

