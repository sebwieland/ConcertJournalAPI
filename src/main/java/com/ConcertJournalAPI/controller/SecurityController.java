package com.ConcertJournalAPI.controller;

import com.ConcertJournalAPI.security.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class SecurityController {


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Claims claims = JwtUtils.parseToken(refreshToken);
            String subject = claims.getSubject();
            Authentication authentication = new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList());
            String newAccessToken = JwtUtils.generateToken(authentication);
            String newRefreshToken = JwtUtils.generateRefreshToken(authentication);

            // Set the new refresh token in a secure cookie
            Cookie newRefreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
            newRefreshTokenCookie.setHttpOnly(true);
            newRefreshTokenCookie.setSecure(true); // Set to true if using HTTPS
            newRefreshTokenCookie.setPath("/");
            response.addCookie(newRefreshTokenCookie);

            return ResponseEntity.ok().body("{\"accessToken\":\"" + newAccessToken + "\"}");
        } catch (JwtException e) {
            // Handle invalid refresh token
            return ResponseEntity.badRequest().build();
        }
    }
}