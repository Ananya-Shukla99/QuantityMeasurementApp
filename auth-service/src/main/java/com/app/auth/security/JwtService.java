package com.app.auth.security;

import com.app.auth.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * JwtService - JWT (JSON Web Token) generation and validation
 *
 * This component handles:
 * - JWT token generation for authenticated users
 * - Token signing with secret key
 * - Token expiration configuration
 *
 * JWT contains:
 * - User email as subject
 * - User role and name as claims
 * - Expiration time
 * - Digital signature
 */
@Component
@Slf4j
public class JwtService {

    /** Secret key for signing JWT tokens (Base64 encoded) */
    @Value("${jwt.secret}")
    private String jwtSecret;

    /** JWT token expiration time in milliseconds */
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Generate JWT token for a user
     *
     * Token includes:
     * - User email as subject (used to identify user)
     * - User role and name as custom claims
     * - Issued at timestamp
     * - Expiration timestamp
     * - Digital signature using secret key
     *
     * @param user User entity to generate token for
     * @return Encoded JWT token string
     */
    public String generateToken(User user) {
        log.info("Generating JWT token for user: {}", user.getEmail());

        String token = Jwts.builder()
                // Add custom claims (role and name)
                .claims(Map.of("role", user.getRole().name(), "name", user.getName()))
                // Set user email as subject (unique identifier)
                .subject(user.getEmail())
                // Set issued at time
                .issuedAt(new Date())
                // Set expiration time
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                // Sign token with secret key
                .signWith(signingKey())
                .compact();

        log.debug("JWT token generated successfully for user: {}", user.getEmail());
        return token;
    }

    /**
     * Get JWT expiration time
     *
     * @return Expiration time in milliseconds
     */
    public long getJwtExpiration() {
        return jwtExpiration;
    }

    /**
     * Decode secret key for token signing
     *
     * Converts Base64 encoded secret to SecretKey object used by JJWT library
     *
     * @return SecretKey for HMAC-SHA algorithm
     */
    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}

