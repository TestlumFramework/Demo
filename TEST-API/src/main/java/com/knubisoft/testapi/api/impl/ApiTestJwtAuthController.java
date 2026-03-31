package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.ApiTestJwtAuthApi;
import com.knubisoft.testapi.dto.auth.jwt.JwtLoginRequest;
import com.knubisoft.testapi.dto.auth.jwt.JwtLoginResponse;
import com.knubisoft.testapi.dto.auth.jwt.JwtUsernameResponse;
import com.knubisoft.testapi.exception.ApiTestAuthException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class ApiTestJwtAuthController implements ApiTestJwtAuthApi {

    private static final String BEARER_PREFIX = "Bearer ";

    private final Map<String, String> users = new ConcurrentHashMap<>();
    private final Map<String, String> tokenStore = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        users.clear();
        users.put("testlum", "123456");
        users.put("testlum1", "123456");
        users.put("testlum2", "123456");
        users.put("testlum3", "123456");
        tokenStore.clear();
    }

    @Override
    public ResponseEntity<JwtLoginResponse> login(final JwtLoginRequest request) {
        final String username = request.getUsername();
        final String password = request.getPassword();

        if (!users.containsKey(username) || !users.get(username).equals(password)) {
            throw new ApiTestAuthException("Invalid credentials");
        }

        final String raw = username + ":" + System.currentTimeMillis();
        final String token = Base64.getEncoder()
                .encodeToString(raw.getBytes(StandardCharsets.UTF_8));

        tokenStore.put(token, username);

        return ResponseEntity.ok(new JwtLoginResponse(token));
    }

    @Override
    public ResponseEntity<JwtUsernameResponse> getProtected(final String authorizationHeader) {
        final String token = extractToken(authorizationHeader);

        final String username = tokenStore.get(token);
        if (username == null) {
            throw new ApiTestAuthException("Invalid or expired token");
        }

        return ResponseEntity.ok()
                .header("X-Auth-Type", "JWT")
                .body(new JwtUsernameResponse(username));
    }
    @Override
    public ResponseEntity<Void> reset() {
        users.clear();
        users.put("testlum", "123456");
        users.put("testlum1", "123456");
        users.put("testlum2", "123456");
        users.put("testlum3", "123456");
        tokenStore.clear();

        return ResponseEntity.ok()
                .header("X-Auth-Type", "JWT")
                .build();
    }

    private String extractToken(final String authorizationHeader) {
        if (StringUtils.isBlank(authorizationHeader)) {
            throw new ApiTestAuthException("Missing Authorization header");
        }

        if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new ApiTestAuthException("Authorization header must start with Bearer");
        }

        return authorizationHeader.substring(BEARER_PREFIX.length());
    }
}