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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class ApiTestJwtAuthController implements ApiTestJwtAuthApi {

    private static final String BEARER_PREFIX = "Bearer ";

    private final Map<String, String> users = new ConcurrentHashMap<>();
    private final Map<String, String> tokens = new ConcurrentHashMap<>();
    private final List<String> protectedStorage = new ArrayList<>();

    @PostConstruct
    public void init() {
        resetState();
    }

    @Override
    public ResponseEntity<JwtLoginResponse> login(final JwtLoginRequest request) {
        final String expectedPassword = users.get(request.getUsername());

        if (expectedPassword == null || !expectedPassword.equals(request.getPassword())) {
            throw new ApiTestAuthException("Invalid username or password");
        }

        final String token = "jwt-token-" + UUID.randomUUID();
        tokens.put(token, request.getUsername());

        return ResponseEntity.ok()
                .header("X-Auth-Type", "JWT")
                .body(new JwtLoginResponse(token));
    }

    @Override
    public ResponseEntity<JwtProtectedResourceResponse> createProtected(
            final String authorizationHeader,
            final JwtProtectedCreateRequest request) {

        validateJwt(authorizationHeader);

        protectedStorage.add(request.getValue());

        return ResponseEntity.ok()
                .header("X-Auth-Type", "JWT")
                .body(new JwtProtectedResourceResponse(new ArrayList<String>(protectedStorage)));
    }

    @Override
    public ResponseEntity<JwtProtectedResourceResponse> getAllProtected(
            final String authorizationHeader) {

        validateJwt(authorizationHeader);

        return ResponseEntity.ok()
                .header("X-Auth-Type", "JWT")
                .body(new JwtProtectedResourceResponse(new ArrayList<String>(protectedStorage)));
    }

    @Override
    public ResponseEntity<Void> reset() {
        resetState();
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

    private void resetState() {
        users.clear();
        tokens.clear();
        protectedStorage.clear();

        users.put("testlum", "123456");
    }
}