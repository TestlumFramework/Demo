package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.ApiTestMicrosoftAuthApi;
import com.knubisoft.testapi.dto.auth.microsoft.MicrosoftCredentialsResponse;
import com.knubisoft.testapi.dto.auth.microsoft.MicrosoftProfileResponse;
import com.knubisoft.testapi.security.microsoft.MicrosoftPrincipal;
import com.knubisoft.testapi.service.MicrosoftOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(name = "microsoft.oauth.enabled", havingValue = "true")
public class ApiTestMicrosoftAuthController implements ApiTestMicrosoftAuthApi {

    private final MicrosoftOAuthService microsoftOAuthService;

    @Override
    public ResponseEntity<MicrosoftProfileResponse> profile(final MicrosoftPrincipal principal) {
        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .body(new MicrosoftProfileResponse(principal.getName(), principal.getEmail()));
    }

    @Override
    public ResponseEntity<Void> authorize(final HttpSession session) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(microsoftOAuthService.authorize(session))
                .header("Cache-Control", "no-store")
                .header("Referrer-Policy", "no-referrer")
                .build();
    }

    @Override
    public ResponseEntity<MicrosoftCredentialsResponse> callback(final String code, final String state,
                                                                final String error, final HttpSession session) {
        MicrosoftCredentialsResponse credentials = microsoftOAuthService.callback(code, state, error, session);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .header("Referrer-Policy", "no-referrer")
                .body(credentials);
    }
}
