package com.app.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OAuth2ResponseDTO - Response object for OAuth2 authentication success
 *
 * This DTO contains user information and OAuth2 provider details
 * after successful authentication via GitHub or Google.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth2ResponseDTO {

    /** JWT token generated after OAuth2 authentication */
    private String token;

    /** User's email address from OAuth2 provider */
    private String email;

    /** User's name from OAuth2 provider */
    private String name;

    /** OAuth2 provider identifier (GITHUB or GOOGLE) */
    private String provider;

    /** User's avatar/profile image URL */
    private String avatarUrl;

    /** OAuth2 provider's unique user ID */
    private String providerId;

    /** Token type (Bearer) */
    private String tokenType = "Bearer";

    /** JWT token expiration time in milliseconds */
    private long expiresIn;

    /** User's role */
    private String role;

    /** Flag indicating if user was newly created */
    private boolean isNewUser;
}

