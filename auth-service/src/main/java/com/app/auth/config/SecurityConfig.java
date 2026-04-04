package com.app.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Value("${cors.allowed.origins:http://localhost:4200}")
    private String corsAllowedOrigins;

    /**
     * Configure CORS (Cross-Origin Resource Sharing)
     * Allows frontend at different origin to communicate with auth-service
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow requests from specified origins
        configuration.setAllowedOrigins(Arrays.asList(corsAllowedOrigins.split(",")));
        // Allow all HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        // Allow all headers
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        // Set max age for preflight cache (in seconds)
        configuration.setMaxAge(3600L);

        // Apply this configuration to all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Configure HTTP security rules
     * This method defines who can access what endpoints
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Apply CORS configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Disable CSRF (Cross-Site Request Forgery protection)
                // We don't need it because we're using stateless JWT authentication
                .csrf(csrf -> csrf.disable())
                // Set session management to IF_REQUIRED
                // OAuth2 needs sessions to store state parameters temporarily
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                // Configure OAuth2 login
                .oauth2Login(oauth2 -> oauth2
                        // On successful OAuth2 authentication
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        // On failed OAuth2 authentication
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                // Define authorization rules
                .authorizeHttpRequests(auth -> auth
                        // These paths do NOT require authentication
                        .requestMatchers(
                                // Auth endpoints - allow users to register/login without token
                                "/api/v1/auth/**",
                                // OAuth2 endpoints - allow OAuth2 login/callback
                                "/oauth2/**",
                                "/login/**",
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
                        ).permitAll()
                        // All other endpoints require authentication
                        .anyRequest().authenticated());

        return http.build();
    }

    /**
     * Create a password encoder using BCrypt algorithm
     * BCrypt:
     * - Automatically hashes passwords
     * - Adds random salt for extra security
     * - Makes rainbow table attacks infeasible
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
