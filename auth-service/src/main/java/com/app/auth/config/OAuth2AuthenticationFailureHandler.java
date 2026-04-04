package com.app.auth.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2AuthenticationFailureHandler - Handles failed OAuth2 authentication
 *
 * When OAuth2 authentication fails (user denies access, provider error, etc.),
 * this handler redirects to frontend with error message.
 */
@Component
@Slf4j
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${oauth2.redirect.uri:http://localhost:4200/oauth2/callback}")
    private String redirectUri;

    /**
     * Handle OAuth2 authentication failure
     *
     * Redirect to frontend with error information in query parameters
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                       AuthenticationException exception) throws IOException, ServletException {
        log.error("OAuth2 authentication failed: {}", exception.getMessage());

        // Build error redirect URL
        String targetUrl = redirectUri + "?error=" + exception.getMessage();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}

