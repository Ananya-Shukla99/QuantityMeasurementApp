package com.app.auth.service;

import com.app.auth.dto.OAuth2ResponseDTO;
import com.app.auth.model.Role;
import com.app.auth.model.User;
import com.app.auth.repository.UserRepository;
import com.app.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OAuth2Service - Handles OAuth2 authentication and user management
 *
 * This service processes OAuth2 authentication from providers like GitHub and Google.
 * It handles user creation/update and JWT token generation for OAuth2 authenticated users.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2Service {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    /**
     * Process OAuth2 authentication response
     *
     * If user exists: update user info and generate token
     * If user doesn't exist: create new user and generate token
     *
     * @param oAuth2User OAuth2 user details from provider
     * @param provider OAuth2 provider (GITHUB or GOOGLE)
     * @return OAuth2ResponseDTO with JWT token
     */
    @Transactional
    public OAuth2ResponseDTO authenticateOAuth2User(OAuth2User oAuth2User, String provider) {
        // Extract user information from OAuth2 provider
        String email = getEmailFromOAuth2User(oAuth2User, provider);
        String name = getNameFromOAuth2User(oAuth2User, provider);
        String providerId = getProviderIdFromOAuth2User(oAuth2User, provider);
        String avatarUrl = getAvatarUrlFromOAuth2User(oAuth2User, provider);

        log.info("Processing OAuth2 authentication for provider: {}, email: {}", provider, email);

        // Check if user exists
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createNewOAuth2User(email, name, provider, providerId, avatarUrl));

        // Update existing user's OAuth2 info if needed
        if (user.getProvider() == null) {
            user.setProvider(provider);
            user.setProviderId(providerId);
            user.setAvatarUrl(avatarUrl);
            user = userRepository.save(user);
            log.info("Updated existing user with OAuth2 info: {}", email);
        }

        // Generate JWT token
        String token = jwtService.generateToken(user);

        // Return OAuth2 response
        return OAuth2ResponseDTO.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .provider(provider)
                .avatarUrl(user.getAvatarUrl())
                .providerId(user.getProviderId())
                .tokenType("Bearer")
                .expiresIn(jwtService.getJwtExpiration())
                .isNewUser(false)
                .build();
    }

    /**
     * Create a new user from OAuth2 provider
     */
    private User createNewOAuth2User(String email, String name, String provider, String providerId, String avatarUrl) {
        User newUser = User.builder()
                .email(email)
                .name(name)
                .password(null) // OAuth2 users don't have password
                .role(Role.ROLE_USER)
                .provider(provider)
                .providerId(providerId)
                .avatarUrl(avatarUrl)
                .enabled(true)
                .build();

        User saved = userRepository.save(newUser);
        log.info("Created new OAuth2 user: {}, provider: {}", email, provider);
        return saved;
    }

    /**
     * Extract email from OAuth2 user attributes based on provider
     * GitHub may return null email if user has private email setting, so we use login@github.com as fallback
     */
    private String getEmailFromOAuth2User(OAuth2User oAuth2User, String provider) {
        if ("GITHUB".equalsIgnoreCase(provider)) {
            // GitHub may return null email if user has private email setting
            String email = oAuth2User.getAttribute("email");
            if (email == null || email.isEmpty()) {
                // Fallback: use login@github.com as placeholder
                String login = oAuth2User.getAttribute("login");
                return login + "@github.com";
            }
            return email;
        } else if ("GOOGLE".equalsIgnoreCase(provider)) {
            return oAuth2User.getAttribute("email");
        }
        return oAuth2User.getAttribute("email");
    }

    /**
     * Extract name from OAuth2 user attributes based on provider
     */
    private String getNameFromOAuth2User(OAuth2User oAuth2User, String provider) {
        if ("GITHUB".equalsIgnoreCase(provider)) {
            String name = oAuth2User.getAttribute("name");
            return name != null ? name : oAuth2User.getAttribute("login");
        } else if ("GOOGLE".equalsIgnoreCase(provider)) {
            return oAuth2User.getAttribute("name");
        }
        return oAuth2User.getAttribute("name");
    }

    /**
     * Extract provider-specific user ID from OAuth2 user attributes
     */
    private String getProviderIdFromOAuth2User(OAuth2User oAuth2User, String provider) {
        if ("GITHUB".equalsIgnoreCase(provider)) {
            return oAuth2User.getAttribute("id").toString();
        } else if ("GOOGLE".equalsIgnoreCase(provider)) {
            return oAuth2User.getAttribute("sub");
        }
        return oAuth2User.getAttribute("id").toString();
    }

    /**
     * Extract avatar URL from OAuth2 user attributes based on provider
     */
    private String getAvatarUrlFromOAuth2User(OAuth2User oAuth2User, String provider) {
        if ("GITHUB".equalsIgnoreCase(provider)) {
            return oAuth2User.getAttribute("avatar_url");
        } else if ("GOOGLE".equalsIgnoreCase(provider)) {
            return oAuth2User.getAttribute("picture");
        }
        return null;
    }
}

