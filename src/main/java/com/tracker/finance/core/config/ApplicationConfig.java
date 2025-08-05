package com.tracker.finance.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class holds application-wide bean configurations.
 * Separating these from SecurityConfig improves clarity and resolves potential
 * bean loading order issues.
 */
@Configuration
public class ApplicationConfig {

    /**
     * Defines the PasswordEncoder bean that will be used for hashing and verifying
     * passwords.
     * We use BCrypt, which is the industry standard.
     *
     * @return An instance of BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
