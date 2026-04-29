package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.ApiTestCustomAuthApi;
import com.knubisoft.testapi.dto.auth.custom.CustomAuthLoginRequest;
import com.knubisoft.testapi.dto.auth.custom.CustomAuthLoginResponse;
import com.knubisoft.testapi.dto.auth.custom.CustomProtectedResponse;
import com.knubisoft.testapi.exception.ApiTestAuthException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiTestCustomAuthController implements ApiTestCustomAuthApi {

    private static final String EXPECTED_CLIENT_ID = "testlum-client";
    private static final String EXPECTED_CLIENT_SECRET = "super-secret";
    private static final String TOKEN = "custom-auth-token-123";
    private static final String CUSTOM_PREFIX = "Custom ";

    @Override
    public ResponseEntity<CustomAuthLoginResponse> login(final CustomAuthLoginRequest request) {
        if (!EXPECTED_CLIENT_ID.equals(request.getClientId())
                || !EXPECTED_CLIENT_SECRET.equals(request.getClientSecret())) {
            throw new ApiTestAuthException("Invalid custom auth credentials");
        }

        return ResponseEntity.ok()
                .header("X-Auth-Type", "CUSTOM")
                .body(new CustomAuthLoginResponse(TOKEN));
    }

    @Override
    public ResponseEntity<CustomProtectedResponse> getProtectedResource(final String authorizationHeader) {
        validateCustomAuth(authorizationHeader);

        return ResponseEntity.ok()
                .header("X-Auth-Type", "CUSTOM")
                .body(new CustomProtectedResponse(
                        "Custom auth access granted",
                        "CUSTOM"
                ));
    }

    @Override
    public ResponseEntity<Void> reset() {
        return ResponseEntity.ok()
                .header("X-Auth-Type", "CUSTOM")
                .build();
    }

    private void validateCustomAuth(final String authorizationHeader) {
        if (StringUtils.isBlank(authorizationHeader)) {
            throw new ApiTestAuthException("Missing Authorization header");
        }

        if (!authorizationHeader.startsWith(CUSTOM_PREFIX)) {
            throw new ApiTestAuthException("Authorization header must start with Custom");
        }

        final String token = authorizationHeader.substring(CUSTOM_PREFIX.length());

        if (!TOKEN.equals(token)) {
            throw new ApiTestAuthException("Invalid custom auth token");
        }
    }
}