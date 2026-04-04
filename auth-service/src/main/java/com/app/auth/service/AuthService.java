package com.app.auth.service;

import com.app.auth.dto.AuthResponseDTO;
import com.app.auth.dto.LoginRequestDTO;
import com.app.auth.dto.RegisterRequestDTO;
import com.app.auth.model.Role;
import com.app.auth.model.User;
import com.app.auth.repository.UserRepository;
import com.app.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * AuthService - Handles user authentication logic
 *
 * This service manages:
 * - User registration (creating new accounts)
 * - User login (authenticating existing users)
 * - Password verification and encryption
 * - JWT token generation
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Register a new user
     *
     * Process:
     * 1. Check if email already exists
     * 2. Create new user with encrypted password
     * 3. Save to database
     * 4. Generate JWT token
     *
     * @param request Registration request containing email, name, and password
     * @return JWT token and user information
     * @throws ResponseStatusException if email already exists (HTTP 409 Conflict)
     */
    public AuthResponseDTO register(RegisterRequestDTO request) {
        log.info("Processing registration for email: {}", request.getEmail());

        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Registration failed - email already exists: {}", request.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        // Create new user entity
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                // Encrypt password using BCrypt
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .provider("email")  // Mark as email/password authentication
                .enabled(true)
                .build();

        // Save user to database
        User saved = userRepository.save(user);
        log.info("User registered successfully: {}", saved.getEmail());

        // Generate JWT token
        String token = jwtService.generateToken(saved);

        return new AuthResponseDTO(token, saved.getEmail(), saved.getRole().name(), saved.getName(), jwtService.getJwtExpiration());
    }

    /**
     * Login user with email and password
     *
     * Process:
     * 1. Find user by email
     * 2. Verify password
     * 3. Generate JWT token
     *
     * @param request Login request containing email and password
     * @return JWT token and user information
     * @throws ResponseStatusException if credentials are invalid (HTTP 401 Unauthorized)
     */
    public AuthResponseDTO login(LoginRequestDTO request) {
        log.info("Processing login for email: {}", request.getEmail());

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed - user not found: {}", request.getEmail());
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
                });

        // Verify password matches
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed - invalid password for email: {}", request.getEmail());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        log.info("User logged in successfully: {}", user.getEmail());

        // Generate JWT token
        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(token, user.getEmail(), user.getRole().name(), user.getName(), jwtService.getJwtExpiration());
    }
}

