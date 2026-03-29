package com.app.quantitymeasurement.controller;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.quantitymeasurement.dto.request.LoginRequestDTO;
import com.app.quantitymeasurement.dto.request.RegisterRequestDTO;
import com.app.quantitymeasurement.dto.response.AuthResponseDTO;
import com.app.quantitymeasurement.model.User;
import com.app.quantitymeasurement.security.jwt.JwtUtils;
import com.app.quantitymeasurement.security.oauth2.UserService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;  // added

    // ── Register ──────────────────────────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @RequestBody @Valid RegisterRequestDTO request) {

        // Save the user — password gets bcrypt-encoded inside UserService
        User user = userService.register(request);

        // Load the saved user as UserDetails to generate a valid token
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());

        // Generate JWT
        String token = jwtUtils.generateToken(userDetails);

        // Extract role cleanly
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority())
                .orElse("ROLE_USER");

        // Return token immediately — user is logged in right after registering
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new AuthResponseDTO(token, user.getEmail(), role, user.getName()));
    }

    // ── Login ─────────────────────────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority())
                .orElse("ROLE_USER");

        // ── Get full user to access name ──────────────────────────────────────
        User user = userService.findByEmail(request.getEmail());

        return ResponseEntity.ok(
                new AuthResponseDTO(token, userDetails.getUsername(), role, user.getName()));
    }
}









