package com.ConcertJournalAPI.security;

import com.ConcertJournalAPI.DataLoader;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

import static com.ConcertJournalAPI.security.JwtUtils.generateRefreshToken;
import static com.ConcertJournalAPI.security.JwtUtils.generateToken;

public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String accessToken = generateToken(authentication);
        String refreshToken = generateRefreshToken(authentication);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"accessToken\":\"" + accessToken + "\", \"refreshToken\":\"" + refreshToken + "\"}");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        LOGGER.info("UserDetails: {}", userDetails);

        // Store refresh token in cookie
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(86400 * 30); // 30 days
        response.addCookie(cookie);
    }

}