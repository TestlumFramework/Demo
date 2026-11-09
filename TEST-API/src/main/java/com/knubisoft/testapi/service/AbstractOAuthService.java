package com.knubisoft.testapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubisoft.testapi.config.OAuthClientConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Collections;

public abstract class AbstractOAuthService<C extends OAuthClientConfig, T> {

    private static final long AUTHORIZATION_TTL = Duration.ofMinutes(10).toMillis();
    private static final SecureRandom RANDOM = new SecureRandom();

    protected final C config;
    private final RestTemplate restTemplate;
    protected final ObjectMapper objectMapper;

    protected AbstractOAuthService(final C config, final RestTemplate restTemplate,
                                   final ObjectMapper objectMapper) {
        this.config = config;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    protected abstract String providerName();
    protected abstract String authorizationEndpoint();
    protected abstract String tokenEndpoint();
    protected abstract String authorizationScope();
    protected abstract T readTokens(String body);

    protected void customizeAuthorization(final UriComponentsBuilder builder) {
    }

    protected void customizeTokenForm(final MultiValueMap<String, String> form) {
    }

    private String sessionAttribute() {
        return getClass().getName() + ".pending";
    }

    public final URI authorize(final HttpSession session) {
        String state = randomValue();
        String verifier = randomValue();
        session.setAttribute(sessionAttribute(),
                new PendingAuthorization(state, verifier, System.currentTimeMillis()));

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(authorizationEndpoint())
                .queryParam("client_id", config.getClientId())
                .queryParam("redirect_uri", config.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", authorizationScope())
                .queryParam("prompt", "consent")
                .queryParam("state", state)
                .queryParam("code_challenge", challenge(verifier))
                .queryParam("code_challenge_method", "S256");
        customizeAuthorization(builder);
        return builder.build().encode().toUri();
    }

    public final T callback(final String code, final String state,
                         final String error, final HttpSession session) {
        PendingAuthorization pending = consumeAuthorization(state, session);
        if (StringUtils.hasText(error)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, providerName() + " authorization was denied or failed");
        }
        if (!StringUtils.hasText(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing " + providerName() + " authorization code");
        }
        return exchangeCode(code, pending.verifier);
    }

    private PendingAuthorization consumeAuthorization(final String state, final HttpSession session) {
        Object value;
        synchronized (session) {
            value = session.getAttribute(sessionAttribute());
            session.removeAttribute(sessionAttribute());
        }
        if (!(value instanceof PendingAuthorization) || !StringUtils.hasText(state)) {
            throw invalidState();
        }
        PendingAuthorization pending = (PendingAuthorization) value;
        if (System.currentTimeMillis() - pending.createdAt > AUTHORIZATION_TTL
                || !MessageDigest.isEqual(pending.state.getBytes(StandardCharsets.UTF_8),
                state.getBytes(StandardCharsets.UTF_8))) {
            throw invalidState();
        }
        return pending;
    }

    private T exchangeCode(final String code, final String verifier) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", config.getClientId());
        form.add("client_secret", config.getClientSecret());
        form.add("redirect_uri", config.getRedirectUri());
        form.add("grant_type", "authorization_code");
        form.add("code", code);
        form.add("code_verifier", verifier);

        customizeTokenForm(form);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String body;
        try {
            body = restTemplate.postForObject(tokenEndpoint(), new HttpEntity<>(form, headers), String.class);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, providerName() + " token exchange failed");
        }
        return readTokens(body);
    }

    private String randomValue() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String challenge(final String verifier) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(verifier.getBytes(StandardCharsets.US_ASCII));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is unavailable");
        }
    }

    private ResponseStatusException invalidState() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired " + providerName() + " authorization state");
    }

    private static final class PendingAuthorization implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String state;
        private final String verifier;
        private final long createdAt;

        private PendingAuthorization(final String state, final String verifier, final long createdAt) {
            this.state = state;
            this.verifier = verifier;
            this.createdAt = createdAt;
        }
    }
}
