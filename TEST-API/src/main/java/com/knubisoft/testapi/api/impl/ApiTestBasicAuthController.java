package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.ApiTestBasicAuthApi;
import com.knubisoft.testapi.dto.auth.basic.BasicAuthUser;
import com.knubisoft.testapi.dto.auth.basic.BasicCreateUserRequest;
import com.knubisoft.testapi.dto.auth.basic.BasicUserResponse;
import com.knubisoft.testapi.exception.ApiTestAuthException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class ApiTestBasicAuthController implements ApiTestBasicAuthApi {

    private static final String BASIC_PREFIX = "Basic ";

    private final List<BasicAuthUser> users = new CopyOnWriteArrayList<>();

    @PostConstruct
    public void init() {
        users.clear();
        users.add(new BasicAuthUser("testlum", "123456", "ADMIN"));
    }

    @Override
    public ResponseEntity<List<BasicUserResponse>> getUsers(final String authorizationHeader) {
        validateBasicAuth(authorizationHeader);

        final List<BasicUserResponse> response = new ArrayList<>();
        for (BasicAuthUser user : users) {
            response.add(new BasicUserResponse(user.getUsername(), user.getRole()));
        }

        return ResponseEntity.ok()
                .header("X-Auth-Type", "BASIC")
                .header("X-Users-Count", String.valueOf(response.size()))
                .body(response);
    }

    @Override
    public ResponseEntity<BasicUserResponse> createUser(final String authorizationHeader,
                                                        final BasicCreateUserRequest request) {
        validateBasicAuth(authorizationHeader);

        final BasicAuthUser user = new BasicAuthUser(
                request.getUsername(),
                request.getPassword(),
                request.getRole()
        );

        users.add(user);

        return ResponseEntity.ok()
                .header("X-Auth-Type", "BASIC")
                .header("X-Created-User", request.getUsername())
                .body(new BasicUserResponse(user.getUsername(), user.getRole()));
    }

    @Override
    public ResponseEntity<Void> reset() {
        users.clear();
        users.add(new BasicAuthUser("testlum", "123456", "ADMIN"));

        return ResponseEntity.ok()
                .header("X-Auth-Type", "BASIC")
                .build();
    }

    private void validateBasicAuth(final String authorizationHeader) {
        if (StringUtils.isBlank(authorizationHeader)) {
            throw new ApiTestAuthException("Missing Authorization header");
        }

        if (!authorizationHeader.startsWith(BASIC_PREFIX)) {
            throw new ApiTestAuthException("Authorization header must start with Basic");
        }

        final String encodedCredentials = authorizationHeader.substring(BASIC_PREFIX.length());

        final String decodedCredentials;
        try {
            decodedCredentials = new String(
                    Base64.getDecoder().decode(encodedCredentials),
                    StandardCharsets.UTF_8
            );
        } catch (IllegalArgumentException ex) {
            throw new ApiTestAuthException("Invalid Base64 credentials");
        }

        final String[] parts = decodedCredentials.split(":", 2);
        if (parts.length != 2) {
            throw new ApiTestAuthException("Invalid Basic auth credentials format");
        }

        final String username = parts[0];
        final String password = parts[1];

        final boolean valid = users.stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));

        if (!valid) {
            throw new ApiTestAuthException("Invalid username or password");
        }
    }
}