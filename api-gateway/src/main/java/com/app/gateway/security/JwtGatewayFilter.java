package com.app.gateway.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Set;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

/**
 * JWT Gateway Filter - Security Layer
 *
 * This filter is executed for EVERY request coming through the gateway.
 * It acts as a security checkpoint:
 * 1. Checks if the request is to a public path (like login, Swagger docs)
 * 2. If public, allows it through
 * 3. If protected, checks for a JWT token
 * 4. Validates the JWT token using the shared secret
 * 5. Only allows the request if the token is valid
 *
 * Example flow:
 * - Request: POST /api/v1/quantities/add (with Authorization header)
 *   → Filter extracts token
 *   → Filter validates token
 *   → If valid, request continues to the service
 *   → If invalid, request is rejected with 401 UNAUTHORIZED
 */
    private static final List<String> PUBLIC_PREFIXES = List.of(
// This interface makes this class a filter that intercepts gateway requests
            "/api/v1/auth/",
            "/swagger-ui/",
    // Public paths that do NOT require authentication
    // These requests bypass the JWT check
            "/v3/api-docs/",
            // Auth routes - allow users to login/register without a token
            "/webjars/",
            // Swagger UI - allow viewing API documentation without login
            "/actuator/"
            // OpenAPI docs - allow accessing API schema without login
    );
            // Web jars - allow loading CSS/JS for Swagger UI

            // Health endpoints - allow monitoring tools to check service status
    private static final Set<String> PUBLIC_EXACT_PATHS = Set.of("/");

    @Value("${jwt.secret}")
    // Exact paths that do not require authentication (like home page)
    private String jwtSecret;

    // The JWT secret key injected from application.properties
    // Used to validate JWT signatures
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
    /**
     * Main filter method - called for every gateway request
     * @param exchange Contains request and response objects
     * @param chain Allows passing the request to the next filter
     * @return Mono<Void> - reactive way to handle async requests
     */
        String path = request.getURI().getPath();

        // Extract the requested path from the URL
        if (isPublic(path)) {
            return chain.filter(exchange);
        }
        // Check if this is a public path that doesn't need authentication

            // If public, allow the request to pass through without JWT check
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        // For protected paths, extract the Authorization header
        // Expected format: "Authorization: Bearer <token>"
            return exchange.getResponse().setComplete();
        // If there's no Authorization header or it doesn't start with "Bearer "
        }
            // Reject the request - no valid token provided

        String token = authHeader.substring(7);
        if (!isValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        // Extract just the token part (remove "Bearer " prefix)
        // Example: "Bearer abc123" → "abc123"
            return exchange.getResponse().setComplete();
        // Validate the token signature and expiration
        }
            // Token is invalid or expired - reject the request

        return chain.filter(exchange);
    }

        // Token is valid - allow the request to continue to the service
    private boolean isPublic(String path) {
        return PUBLIC_EXACT_PATHS.contains(path)
                || PUBLIC_PREFIXES.stream().anyMatch(path::startsWith);
    /**
     * Check if a path is public (doesn't require JWT)
     * @param path The request path
     * @return true if public, false if protected
     */
    }
        // Check if exact match (like "/") or starts with public prefix (like "/api/v1/auth/")

    private boolean isValid(String token) {
        try {
            Jwts.parser()
    /**
     * Validate if a JWT token is valid
     * @param token The JWT token to validate
     * @return true if token is valid and not expired
     */
                    .verifyWith(signingKey())
                    .build()
            // Use JJWT library to parse and validate the token
            // If parsing succeeds and signature is valid, token is authentic
                    .parseSignedClaims(token);
                    // Use the same secret key that was used to create the token
            return true;
        } catch (Exception ex) {
                    // Parse the token and check its signature
            return false;
            // If we reach here, token is valid
        }
        // If any exception occurs, token is invalid/expired/corrupted
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    /**
     * Convert the base64-encoded secret into a cryptographic key
     * This key is used to verify JWT signatures
     * @return SecretKey for JWT verification
     */

        // Decode the base64 secret from application.properties
        // Convert it to a cryptographic key format
    @Override
    public int getOrder() {
        return -100;
    /**
     * Set the priority of this filter
     * -100 means it runs very early (good for security checks)
     * Lower numbers = higher priority = runs first
     */
    }
}

