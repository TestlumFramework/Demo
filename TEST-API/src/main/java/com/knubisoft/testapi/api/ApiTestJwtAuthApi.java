package com.knubisoft.testapi.api;

import com.knubisoft.testapi.dto.auth.jwt.JwtLoginRequest;
import com.knubisoft.testapi.dto.auth.jwt.JwtLoginResponse;
import com.knubisoft.testapi.dto.auth.jwt.JwtProtectedCreateRequest;
import com.knubisoft.testapi.dto.auth.jwt.JwtProtectedResourceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/test/auth/jwt")
public interface ApiTestJwtAuthApi {

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<JwtLoginResponse> login(@RequestBody JwtLoginRequest request);

    @PostMapping("/resource")
    @Operation(summary = "Create protected resource")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<JwtProtectedResourceResponse> createProtected(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody JwtProtectedCreateRequest request
    );

    @GetMapping("/resources")
    @Operation(summary = "Get all protected resources")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<JwtProtectedResourceResponse> getAllProtected(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    );

    @DeleteMapping("/reset")
    @Operation(summary = "Reset JWT auth state", description = "Reset JWT users, tokens and resources to default state")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<Void> reset();
}