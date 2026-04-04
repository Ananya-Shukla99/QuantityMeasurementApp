package com.app.qma.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration for QMA Service
 *
 * This class defines security rules for the qma-service.
 * It specifies which endpoints are public and which require JWT authentication.
 *
 * Key difference from Auth Service:
 * - Auth Service allows /api/v1/auth/** without a token
 * - QMA Service allows only "/" (root), health, Swagger without a token
 * - All quantity endpoints require a valid JWT token
 *
 * Security flow:
 * 1. Request comes to QMA service
 * 2. First, JwtAuthFilter checks for JWT token
 * 3. If token is valid, Spring Security processes the request
 * 4. Then this SecurityConfig decides if the endpoint is public or protected
 */
@Configuration  // Marks this class as a Spring configuration class
@EnableWebSecurity  // Activates Spring Security for this service
public class SecurityConfig {

    // JWT filter that validates tokens
    private final JwtAuthFilter jwtAuthFilter;

    // Constructor - Spring automatically provides the JwtAuthFilter bean
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Configure HTTP security rules
     * This method defines who can access what endpoints
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (Cross-Site Request Forgery protection)
                // We're using stateless JWT authentication instead
                .csrf(csrf -> csrf.disable())
                // Set session management to STATELESS
                // Each request must include the JWT token (no stored sessions)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Define authorization rules
                .authorizeHttpRequests(auth -> auth
                        // These paths do NOT require authentication
                        .requestMatchers(
                                // Root path - allow access
                                "/",
                                // Health endpoint - allow monitoring tools to check if service is up
                                "/actuator/health",
                                // OpenAPI documentation - allow viewing API schema
                                "/v3/api-docs/**",
                                // Swagger UI - allow viewing interactive API documentation
                                "/swagger-ui/**",
                                // Swagger UI HTML file
                                "/swagger-ui.html",
                                // Web resources for Swagger UI (CSS, JS files)
                                "/webjars/**"
                        ).permitAll()  // Allow access without authentication
                        // All other endpoints require authentication (JWT token)
                        .anyRequest().authenticated())
                // Add the JWT filter BEFORE the default authentication filter
                // This ensures JWT validation happens first
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
