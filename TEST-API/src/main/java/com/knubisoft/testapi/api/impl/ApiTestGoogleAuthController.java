package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.ApiTestGoogleAuthApi;
import com.knubisoft.testapi.dto.auth.google.GoogleCredentialsResponse;
import com.knubisoft.testapi.dto.auth.google.GoogleProfileResponse;
import com.knubisoft.testapi.security.google.GooglePrincipal;
import com.knubisoft.testapi.service.GoogleOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(name = "google.oauth.enabled", havingValue = "true")
public class ApiTestGoogleAuthController implements ApiTestGoogleAuthApi {

    private final GoogleOAuthService googleOAuthService;

    @Override
    public ResponseEntity<GoogleProfileResponse> profile(final GooglePrincipal principal) {
        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .body(new GoogleProfileResponse(principal.getName(), principal.getEmail()));
    }

    @Override
    public ResponseEntity<Void> authorize(final HttpSession session) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(googleOAuthService.authorize(session))
                .header("Cache-Control", "no-store")
                .header("Referrer-Policy", "no-referrer")
                .build();
    }

    @Override
    public ResponseEntity<GoogleCredentialsResponse> callback(final String code, final String state,
                                         final String error, final HttpSession session) {
        String refreshToken = googleOAuthService.callback(code, state, error, session);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .header("Referrer-Policy", "no-referrer")
                .body(new GoogleCredentialsResponse(refreshToken));
    }
}
