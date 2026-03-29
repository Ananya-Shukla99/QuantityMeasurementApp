package com.app.quantitymeasurement.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    // ── Validate config on startup ─────────────────────────────────────────────
    @PostConstruct
    public void validateConfig() {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            throw new IllegalStateException(
                "JWT secret is not configured. " +
                "Set the JWT_SECRET environment variable before starting the application."
            );
        }
        if (jwtSecret.length() < 32) {
            throw new IllegalStateException(
                "JWT secret is too short — minimum 32 characters required. " +
                "Generate one with: openssl rand -hex 32"
            );
        }
        if (jwtExpirationMs <= 0) {
            throw new IllegalStateException(
                "JWT expiration must be a positive number of milliseconds. " +
                "Check the JWT_EXPIRATION environment variable."
            );
        }
        log.info("JWT configuration validated — expiration: {}ms", jwtExpirationMs);
    }

    // ── Generate token ─────────────────────────────────────────────────────────
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    // ── Extract username from token ────────────────────────────────────────────
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ── Validate token against UserDetails ────────────────────────────────────
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // ── Check expiry ───────────────────────────────────────────────────────────
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // ── Parse all claims ───────────────────────────────────────────────────────
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ── Build signing key from secret ──────────────────────────────────────────
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}