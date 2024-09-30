package com.ConcertJournalAPI.configuration;

import com.ConcertJournalAPI.security.AuthHandlers;
import com.ConcertJournalAPI.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Enable CORS
                .cors(cors -> CorsConfig.corsConfigurationSource())

                // Disable CSRF for simplicity (enable it in production)
                .csrf(AbstractHttpConfigurer::disable)

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )

                .formLogin(form -> form
                        .usernameParameter("email")
                        .successHandler(new AuthHandlers.AuthSuccessHandler())
                        .failureHandler(new AuthHandlers.AuthFailureHandler())
                )

                // Authorize requests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error", "/auth/**", "/register", "/login").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyRequest().authenticated()
                )
                // Use HTTP Basic Authentication (for simplicity)
                .httpBasic(withDefaults())

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new AuthHandlers.CustomAuthenticationEntryPoint()))

                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}


