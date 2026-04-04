package com.app.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Entity - Represents user information in the database
 *
 * This class stores user credentials and OAuth provider information.
 * Users can register using email/password or authenticate via OAuth2 (GitHub/Google).
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /** Unique identifier for the user */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User's email - unique identifier for login */
    @Column(unique = true, nullable = false)
    private String email;

    /** User's display name */
    @Column(nullable = false)
    private String name;

    /** Encrypted password - null if user registered via OAuth2 */
    @Column(nullable = true)
    private String password;

    /** User's role/authority level */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.ROLE_USER;

    /** OAuth2 provider (GITHUB, GOOGLE, or null for email/password auth) */
    @Column(nullable = true)
    private String provider;

    /** Provider-specific user ID for OAuth2 users */
    @Column(nullable = true)
    private String providerId;

    /** User's avatar URL from OAuth2 provider */
    @Column(nullable = true)
    private String avatarUrl;

    /** Flag to track if user is enabled */
    @Column(nullable = false)
    private Boolean enabled = true;
}

