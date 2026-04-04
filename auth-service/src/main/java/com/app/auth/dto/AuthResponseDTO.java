package com.app.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthResponseDTO - Response object returned after successful authentication
 *
 * This DTO contains JWT token and user information after login/registration
 * or OAuth2 authentication.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {

    /** JWT token for authentication */
    private String token;

    /** User's email address */
    private String email;

    /** User's role/authority */
    private String role;

    /** User's display name */
    private String name;

    /** Token type (e.g., "Bearer") */
    private String tokenType;

    /** Token expiration time in milliseconds */
    private long expiresIn;

    /** Authentication provider used (e.g., "email", "github", "google") */
    private String provider;

    /** User's avatar URL if available */
    private String avatarUrl;

    /**
     * Constructor for email/password authentication response
     */
    public AuthResponseDTO(String token, String email, String role, String name, long expiresIn) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.name = name;
        this.tokenType = "Bearer";
        this.expiresIn = expiresIn;
        this.provider = "email";
    }
}

