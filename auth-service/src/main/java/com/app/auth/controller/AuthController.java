package com.app.auth.controller;

import com.app.auth.dto.AuthResponseDTO;
import com.app.auth.dto.LoginRequestDTO;
import com.app.auth.dto.RegisterRequestDTO;
import com.app.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController - Handles authentication endpoints
 *
 * This controller provides REST APIs for:
 * 1. User registration (email/password)
 * 2. User login (email/password)
 * 3. OAuth2 authentication (GitHub/Google)
 *
 * All endpoints are public and accessible without JWT token.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user with email and password
     *
     * @param request Registration request with email, name, and password
     * @return JWT token and user information
     */
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account with email and password")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
        log.info("User registration request for email: {}", request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    /**
     * Login user with email and password
     *
     * @param request Login request with email and password
     * @return JWT token and user information
     */
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user with email and password, returns JWT token")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        log.info("User login request for email: {}", request.getEmail());
        return ResponseEntity.ok(authService.login(request));
    }
    /**
     * Logout endpoint
     *
     * Since we're using stateless JWT authentication,
     * logout is handled on the client side by removing the token.
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Logout user by removing JWT token on client side")
    public ResponseEntity<String> logout() {
        log.info("User logout request");
        return ResponseEntity.ok("Logged out successfully. Please remove the JWT token from client.");
    }
}

