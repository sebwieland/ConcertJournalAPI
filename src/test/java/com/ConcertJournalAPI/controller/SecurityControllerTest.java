package com.ConcertJournalAPI.controller;

import com.ConcertJournalAPI.security.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityControllerTest {

    @Mock
    private Authentication authentication;

    @Mock
    private HttpServletRequest request;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private SecurityController securityController;

    @Test
    void testRefreshAccessToken_RefreshTokenNotFound_ReturnsBadRequest() {
        // Arrange
        when(request.getCookies()).thenReturn(null);

        // Act
        ResponseEntity<?> response = securityController.refreshAccessToken(request);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testRefreshAccessToken_RefreshTokenFoundButInvalid_ReturnsBadRequest() {
        // Arrange
        Cookie[] cookies = new Cookie[]{new Cookie("refreshToken", "invalidToken")};
        when(request.getCookies()).thenReturn(cookies);

        // Act
        ResponseEntity<?> response = securityController.refreshAccessToken(request);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testRefreshAccessToken_RefreshTokenFoundAndValid_ReturnsOkWithNewAccessToken() {
        // Arrange
        String validToken = JwtUtils.generateToken(authentication);
        Cookie[] cookies = new Cookie[]{new Cookie("refreshToken", validToken)};
        when(request.getCookies()).thenReturn(cookies);
        Claims claims = mock(Claims.class);

        // Act
        ResponseEntity<?> response = securityController.refreshAccessToken(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody() instanceof String);
        String responseBody = (String) response.getBody();
        String accessToken = responseBody.substring(responseBody.indexOf(":") + 2, responseBody.length() - 2);
        try {
            JwtUtils.parseToken(accessToken);
        } catch (JwtException e) {
            Assertions.fail("Invalid token");
        }
    }
}