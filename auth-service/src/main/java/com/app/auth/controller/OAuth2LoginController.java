package com.app.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@RestController
@RequestMapping("/oauth2")
@Slf4j
@Tag(name = "OAuth2 Login", description = "OAuth2 login initiation endpoints")
public class OAuth2LoginController {

    /**
     * Initiate GitHub OAuth2 login flow
     *
     * Redirects user to Spring Security's OAuth2 login endpoint for GitHub
     *
     * @return Redirect to /login/oauth2/authorization/github
     */
    @GetMapping("/authorize/github")
    @Operation(
        summary = "Initiate GitHub OAuth2 Login",
        description = "Redirects to GitHub login page. User will be redirected back to /login/oauth2/code/github after approval."
    )
    public RedirectView authorizeGithub() {
        log.info("GitHub OAuth2 authorization initiated");
        // Spring Security OAuth2 login endpoint
        return new RedirectView("/login/oauth2/authorization/github", true);
    }

    /**
     * Direct GitHub OAuth2 authorization endpoint
     * This handles the /oauth2/authorization/github path directly from the API gateway
     *
     * @return Redirect to /login/oauth2/authorization/github
     */
    @GetMapping("/authorization/github")
    @Operation(
        summary = "GitHub OAuth2 Authorization",
        description = "Direct endpoint for GitHub OAuth2 authorization flow"
    )
    public RedirectView authorizationGithub() {
        log.info("GitHub OAuth2 authorization initiated (direct endpoint)");
        return new RedirectView("/login/oauth2/authorization/github", true);
    }

    /**
     * Initiate Google OAuth2 login flow
     *
     * Redirects user to Spring Security's OAuth2 login endpoint for Google
     *
     * @return Redirect to /login/oauth2/authorization/google
     */
    @GetMapping("/authorize/google")
    @Operation(
        summary = "Initiate Google OAuth2 Login",
        description = "Redirects to Google login page. User will be redirected back to /login/oauth2/code/google after approval."
    )
    public RedirectView authorizeGoogle() {
        log.info("Google OAuth2 authorization initiated");
        // Spring Security OAuth2 login endpoint
        return new RedirectView("/login/oauth2/authorization/google", true);
    }

    /**
     * Direct Google OAuth2 authorization endpoint
     * This handles the /oauth2/authorization/google path directly from the API gateway
     *
     * @return Redirect to /login/oauth2/authorization/google
     */
    @GetMapping("/authorization/google")
    @Operation(
        summary = "Google OAuth2 Authorization",
        description = "Direct endpoint for Google OAuth2 authorization flow"
    )
    public RedirectView authorizationGoogle() {
        log.info("Google OAuth2 authorization initiated (direct endpoint)");
        return new RedirectView("/login/oauth2/authorization/google", true);
    }

    /**
     * Fallback endpoint for OAuth2 authorization
     * Useful for testing if OAuth2 is configured correctly
     */
    @GetMapping("/config-test")
    @Operation(
        summary = "Test OAuth2 Configuration",
        description = "Test endpoint to verify OAuth2 is properly configured"
    )
    public ResponseEntity<String> testOAuth2Config() {
        String testInfo = "OAuth2 Configuration Test:\n" +
                "1. Visit http://localhost:8081/oauth2/authorize/github to login with GitHub\n" +
                "2. Visit http://localhost:8081/oauth2/authorize/google to login with Google\n" +
                "3. Both should redirect to the OAuth2 provider login pages";
        log.info(testInfo);
        return ResponseEntity.ok(testInfo);
    }
}

