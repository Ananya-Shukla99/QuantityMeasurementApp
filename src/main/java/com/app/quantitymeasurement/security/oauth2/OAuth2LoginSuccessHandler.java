package com.app.quantitymeasurement.security.oauth2;

import com.app.quantitymeasurement.enums.Role;
import com.app.quantitymeasurement.model.User;
import com.app.quantitymeasurement.repository.UserRepository;
import com.app.quantitymeasurement.security.jwt.JwtUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        if (response.isCommitted()) {
            log.warn("Response already committed — cannot redirect");
            return;
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // ── Detect which provider (github or google) ──────────────────────────
        String provider = ((OAuth2AuthenticationToken) authentication)
                .getAuthorizedClientRegistrationId();
        log.info("OAuth2 login via provider: {}", provider);

        // ── Extract profile data — works for both GitHub and Google ───────────
        String email = extractEmail(oAuth2User, provider);
        String name  = extractName(oAuth2User);

        if (email == null || email.isBlank()) {
            log.error("OAuth2 login failed — no email returned from provider: {}", provider);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Email not available from " + provider + ". " +
                    "Please make sure your email is public and try again.");
            return;
        }

        // ── Auto-register on first login, or load existing user ───────────────
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> registerNewOAuthUser(email, name, provider));

        log.info("OAuth2 login success for: {} via {}", email, provider);

        // ── Generate JWT ──────────────────────────────────────────────────────
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtils.generateToken(userDetails);

        // ── Redirect to frontend with token ───────────────────────────────────
        String targetUrl = UriComponentsBuilder
                .fromUriString(redirectUri)
                .queryParam("token", token)
                .build()
                .toUriString();

        log.debug("Redirecting OAuth2 user to: {}", targetUrl);

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    // ── Register new OAuth2 user (GitHub or Google) ───────────────────────────
    private User registerNewOAuthUser(String email, String name, String provider) {
        log.info("Registering new {} OAuth2 user: {}", provider, email);

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name != null ? name : extractUsernameFromEmail(email));
        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        newUser.setRole(Role.ROLE_USER);

        return userRepository.save(newUser);
    }

    // ── Extract email — handles GitHub private email fallback ─────────────────
    private String extractEmail(OAuth2User oAuth2User, String provider) {
        // Google always returns email directly
        // GitHub returns email if public, otherwise null
        String email = oAuth2User.getAttribute("email");
        if (email != null && !email.isBlank()) {
            return email;
        }

        // GitHub-specific fallback for private emails
        if ("github".equals(provider)) {
            String login = oAuth2User.getAttribute("login");
            if (login != null && !login.isBlank()) {
                log.warn("No public email for GitHub user '{}' — using noreply fallback", login);
                return login + "@users.noreply.github.com";
            }
        }

        return null;
    }

    // ── Extract name — works for both providers ───────────────────────────────
    private String extractName(OAuth2User oAuth2User) {
        // Both Google and GitHub return "name"
        String name = oAuth2User.getAttribute("name");
        if (name != null && !name.isBlank()) {
            return name;
        }
        // GitHub fallback
        String login = oAuth2User.getAttribute("login");
        if (login != null) return login;

        // Google fallback
        return oAuth2User.getAttribute("given_name");
    }

    // ── Derive name from email as last resort ─────────────────────────────────
    private String extractUsernameFromEmail(String email) {
        return email.substring(0, email.indexOf('@'));
    }
}