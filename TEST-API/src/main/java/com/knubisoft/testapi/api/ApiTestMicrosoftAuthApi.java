package com.knubisoft.testapi.api;

import com.knubisoft.testapi.dto.auth.microsoft.MicrosoftCredentialsResponse;
import com.knubisoft.testapi.dto.auth.microsoft.MicrosoftProfileResponse;
import com.knubisoft.testapi.security.microsoft.MicrosoftPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@RequestMapping("/api/test/auth/microsoft")
public interface ApiTestMicrosoftAuthApi {

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Read name and email using a Microsoft Graph access token")
    ResponseEntity<MicrosoftProfileResponse> profile(@AuthenticationPrincipal MicrosoftPrincipal principal);

    @GetMapping("/authorize")
    @Operation(summary = "Start Microsoft authorization")
    ResponseEntity<Void> authorize(HttpSession session);

    @GetMapping(value = "/callback", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Microsoft access and refresh tokens from an authorization code")
    ResponseEntity<MicrosoftCredentialsResponse> callback(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "error", required = false) String error,
            HttpSession session
    );
}
