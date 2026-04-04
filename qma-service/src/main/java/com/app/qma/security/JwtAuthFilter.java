package com.app.qma.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT Authentication Filter for QMA Service
 *
 * This filter runs on every request to the QMA service.
 * Its job is to:
 * 1. Extract the JWT token from the Authorization header
 * 2. Parse the JWT token
 * 3. Extract user information (email, role) from the token
 * 4. Tell Spring Security that the user is authenticated
 *
 * This allows the QMA service to validate JWT tokens issued by the Auth Service.
 *
 * The flow is:
 * - Request arrives with: Authorization: Bearer <jwt-token>
 * - Filter extracts the token
 * - Filter parses the token using JwtService
 * - Filter creates a Spring Security authentication object
 * - Spring Security knows the user is authenticated
 * - Request is allowed to continue to the controller
 *
 * If the token is missing or invalid:
 * - Filter clears the security context
 * - Request continues but without authentication
 * - The endpoint will return 403 Forbidden if it requires authentication
 */
@Component  // Register this as a Spring Bean so it's automatically used as a filter
public class JwtAuthFilter extends OncePerRequestFilter {

    // Service that handles JWT parsing and validation
    private final JwtService jwtService;

    // Constructor - Spring automatically provides the JwtService bean
    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * This method is called for every HTTP request to the service
     * The "OncePerRequestFilter" ensures this method runs exactly once per request
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get the Authorization header from the request
        // Expected format: "Bearer <jwt-token>"
        String authHeader = request.getHeader("Authorization");

        // If there's no Authorization header or it doesn't start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No token provided - let the request pass through
            // The SecurityConfig will reject it if the endpoint requires auth
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extract just the token part (remove "Bearer " prefix)
            String token = authHeader.substring(7);

            // Parse the JWT token and extract claims (user data)
            Claims claims = jwtService.parse(token);

            // Get the email address from the token (usually the subject)
            String email = claims.getSubject();

            // Get the role from the token claims (e.g., "USER", "ADMIN")
            String role = String.valueOf(claims.get("role"));

            // Create a Spring Security authentication object
            // This tells Spring Security that this user is authenticated
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    email,  // The principal (user identifier)
                    null,   // No password needed (we already validated the token)
                    List.of(new SimpleGrantedAuthority(role))  // User's role/authority
            );

            // Set this authentication in the security context
            // Now Spring Security knows the user is logged in
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception ignored) {
            // If anything goes wrong while parsing the token:
            // - Invalid signature
            // - Expired token
            // - Corrupted token
            // Clear the security context to ensure the user is not authenticated
            SecurityContextHolder.clearContext();
        }

        // Continue with the request (pass it to the next filter or controller)
        filterChain.doFilter(request, response);
    }
}
