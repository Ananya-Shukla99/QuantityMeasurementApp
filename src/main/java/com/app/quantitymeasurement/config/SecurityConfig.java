// src/main/java/com/app/quantitymeasurement/config/SecurityConfig.java

package com.app.quantitymeasurement.config;

import com.app.quantitymeasurement.security.jwt.JwtAuthFilter;
import com.app.quantitymeasurement.security.oauth2.OAuth2LoginSuccessHandler;
import com.app.quantitymeasurement.security.oauth2.UserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final PasswordEncoder passwordEncoder; // injected, not declared here

    @Value("${app.cors.allowed-origins:https://quantity-measurement-app-frontend-olive.vercel.app,http://localhost:4200,http://localhost:4300,http://localhost:3000,http://localhost:8080}")
    private List<String> allowedOrigins;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          UserDetailsServiceImpl userDetailsService,
                          OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,
                          PasswordEncoder passwordEncoder) {  // injected from PasswordEncoderConfig
        this.jwtAuthFilter             = jwtAuthFilter;
        this.userDetailsService        = userDetailsService;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
        this.passwordEncoder           = passwordEncoder;
    }

    // ── Main filter chain ─────────────────────────────────────────────────────
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter,
                    UsernamePasswordAuthenticationFilter.class)
            .oauth2Login(oauth -> oauth
                    .successHandler(oAuth2LoginSuccessHandler)
                    .failureHandler((request, response, exception) -> {
                        log.error("OAuth2 login failed: {}", exception.getMessage());
                        // Redirect to frontend login with error
                        String frontendUrl = "https://quantity-measurement-app-frontend-olive.vercel.app/login?error=oauth2_failed";
                        response.sendRedirect(frontendUrl);
                    })
            )
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, authException) -> {
                        log.warn("Unauthorized request to: {}",
                                request.getRequestURI());
                        response.sendError(
                            jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED,
                            "Unauthorized — please log in"
                        );
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        log.warn("Access denied to: {}", request.getRequestURI());
                        response.sendError(
                            jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN,
                            "Forbidden — you don't have permission"
                        );
                    })
            )
            .headers(headers -> headers
                    .frameOptions(frame -> frame.disable())
            );

        return http.build();
    }

    // ── Public endpoints ──────────────────────────────────────────────────────
    private static final String[] PUBLIC_ENDPOINTS = {
        "/api/v1/auth/**",
        "/oauth2/**",
        "/login/oauth2/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**",
        "/swagger-resources/**",
        "/webjars/**",
        "/h2-console/**",
        "/actuator/health",
    };

    // ── AuthenticationProvider ────────────────────────────────────────────────
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder); // injected, not created here
        return provider;
    }

    // ── AuthenticationManager ─────────────────────────────────────────────────
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ── CORS ──────────────────────────────────────────────────────────────────
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));
        config.setAllowedHeaders(List.of(
                "Authorization", "Content-Type", "Accept", "X-Requested-With"
        ));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}