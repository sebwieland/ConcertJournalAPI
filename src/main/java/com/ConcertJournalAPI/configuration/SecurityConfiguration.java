package com.ConcertJournalAPI.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for simplicity (enable it in production)
                .csrf(csrf -> csrf.disable())
                // Authorize requests
                .authorizeHttpRequests(auth -> auth
                        // Permit access to the registration and login endpoints
                        .requestMatchers("/auth/**", "/users/register").permitAll()
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                // Use HTTP Basic Authentication (for simplicity)
                .httpBasic();

        return http.build();
    }

    // Expose AuthenticationManager bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}


