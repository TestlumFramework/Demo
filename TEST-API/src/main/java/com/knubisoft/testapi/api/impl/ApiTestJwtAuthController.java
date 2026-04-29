package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.ApiTestJwtAuthApi;
import com.knubisoft.testapi.dto.auth.jwt.JwtLoginRequest;
import com.knubisoft.testapi.dto.auth.jwt.JwtLoginResponse;
import com.knubisoft.testapi.dto.auth.jwt.JwtProtectedCreateRequest;
import com.knubisoft.testapi.dto.auth.jwt.JwtProtectedResourceResponse;
import com.knubisoft.testapi.exception.ApiTestAuthException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class ApiTestJwtAuthController implements ApiTestJwtAuthApi {

    private static final String BEARER_PREFIX = "Bearer ";

    private final Map<String, String> users = new ConcurrentHashMap<>();
    private final Map<String, String> tokens = new ConcurrentHashMap<>();
    private final Map<String, CopyOnWriteArrayList<String>> storageByToken = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        users.put("testlum", "123456");
    }

    @Override
    public ResponseEntity<JwtLoginResponse> login(final JwtLoginRequest request) {
        final String expectedPassword = users.get(request.getUsername());

        if (expectedPassword == null || !expectedPassword.equals(request.getPassword())) {
            throw new ApiTestAuthException("Invalid username or password");
        }

        final String token = "jwt-token-" + UUID.randomUUID();
        tokens.put(token, request.getUsername());
        storageByToken.put(token, new CopyOnWriteArrayList<>());

        return ResponseEntity.ok()
                .header("X-Auth-Type", "JWT")
                .body(new JwtLoginResponse(token));
    }

    @Override
    public ResponseEntity<JwtProtectedResourceResponse> createProtected(
            final String authorizationHeader,
            final JwtProtectedCreateRequest request) {

        final String token = validateJwt(authorizationHeader);

        storageByToken
                .computeIfAbsent(token, key -> new CopyOnWriteArrayList<>())
                .add(request.getValue());

        return ResponseEntity.ok()
                .header("X-Auth-Type", "JWT")
                .body(new JwtProtectedResourceResponse(new ArrayList<>(storageByToken.get(token))));
    }

    @Override
    public ResponseEntity<JwtProtectedResourceResponse> getAllProtected(final String authorizationHeader) {
        final String token = validateJwt(authorizationHeader);

        return ResponseEntity.ok()
                .header("X-Auth-Type", "JWT")
                .body(new JwtProtectedResourceResponse(
                        new ArrayList<>(storageByToken.getOrDefault(token, new CopyOnWriteArrayList<>()))
                ));
    }

    @Override
    public ResponseEntity<Void> reset() {
        return ResponseEntity.ok()
                .header("X-Auth-Type", "JWT")
                .build();
    }

    private String validateJwt(final String authorizationHeader) {
        if (StringUtils.isBlank(authorizationHeader)) {
            throw new ApiTestAuthException("Missing Authorization header");
        }

        if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new ApiTestAuthException("Authorization header must start with Bearer");
        }

        final String token = authorizationHeader.substring(BEARER_PREFIX.length());

        if (!tokens.containsKey(token)) {
            throw new ApiTestAuthException("Invalid or expired token");
        }

        return token;
    }
}