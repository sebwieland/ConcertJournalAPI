package com.ConcertJournalAPI.configuration;

import com.ConcertJournalAPI.security.AuthFailureHandler;
import com.ConcertJournalAPI.security.AuthSuccessHandler;
import com.ConcertJournalAPI.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthSuccessHandler authSuccessHandler = new AuthSuccessHandler();
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
                        .successHandler(new AuthSuccessHandler())
                        .failureHandler(new AuthFailureHandler())
                        .loginPage("/login")
                )

                // Authorize requests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error", "/auth/**", "/register", "/login", "/actuator/prometheus").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyRequest().authenticated()
                )
                // Use HTTP Basic Authentication (for simplicity)
                //.httpBasic(withDefaults())

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                )

                .addFilterBefore(new JwtAuthenticationFilter(authSuccessHandler), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}


