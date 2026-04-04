package com.app.auth.config;

import com.app.auth.dto.OAuth2ResponseDTO;
import com.app.auth.service.OAuth2Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * OAuth2AuthenticationSuccessHandler - Handles successful OAuth2 authentication
 *
 * After successful OAuth2 authentication from GitHub or Google,
 * this handler processes the OAuth2 user data and redirects to frontend
 * with JWT token in URL parameters.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuth2Service oAuth2Service;

    @Value("${oauth2.redirect.uri:http://localhost:4200/oauth2/callback}")
    private String redirectUri;

    /**
     * Handle successful OAuth2 authentication
     *
     * Process:
     * 1. Extract OAuth2 user details and provider information
     * 2. Create or update user in database
     * 3. Generate JWT token
     * 4. Redirect to frontend with token as URL parameter
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
        try {
            // Get OAuth2 user details
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            // Determine which OAuth2 provider was used
            String provider = getProviderFromAuthentication(authentication);
            log.info("OAuth2 authentication success for provider: {}", provider);

            // Process OAuth2 user and generate JWT token
            OAuth2ResponseDTO oAuth2Response = oAuth2Service.authenticateOAuth2User(oAuth2User, provider);

            // Redirect to frontend with token and user information
            String targetUrl = buildRedirectUrl(oAuth2Response);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        } catch (Exception e) {
            log.error("Error handling OAuth2 authentication success", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "OAuth2 authentication failed");
        }
    }

    /**
     * Build redirect URL with JWT token and user information as query parameters
     */
    private String buildRedirectUrl(OAuth2ResponseDTO oAuth2Response) {
        StringBuilder urlBuilder = new StringBuilder(redirectUri);
        urlBuilder.append("?");
        urlBuilder.append("token=").append(encode(oAuth2Response.getToken()));
        urlBuilder.append("&email=").append(encode(oAuth2Response.getEmail()));
        urlBuilder.append("&name=").append(encode(oAuth2Response.getName()));
        urlBuilder.append("&provider=").append(encode(oAuth2Response.getProvider()));

        if (oAuth2Response.getAvatarUrl() != null) {
            urlBuilder.append("&avatarUrl=").append(encode(oAuth2Response.getAvatarUrl()));
        }

        urlBuilder.append("&role=").append(encode(oAuth2Response.getRole()));
        urlBuilder.append("&expiresIn=").append(oAuth2Response.getExpiresIn());

        return urlBuilder.toString();
    }

    /**
     * URL encode parameter values
     */
    private String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            log.error("Error encoding URL parameter", e);
            return value;
        }
    }

    /**
     * Determine OAuth2 provider name from authentication object
     */
    private String getProviderFromAuthentication(Authentication authentication) {
        // The provider name is available in the authentication details
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            return token.getAuthorizedClientRegistrationId().toUpperCase();
        }

        // Fallback: try to get from principal name or other sources
        return "UNKNOWN";
    }
}

