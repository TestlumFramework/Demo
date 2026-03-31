package com.knubisoft.testapi.api;

import com.knubisoft.testapi.dto.auth.jwt.JwtLoginRequest;
import com.knubisoft.testapi.dto.auth.jwt.JwtLoginResponse;
import com.knubisoft.testapi.dto.auth.jwt.JwtUsernameResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/test/auth/jwt")
public interface ApiTestJwtAuthApi {

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<JwtLoginResponse> login(@RequestBody JwtLoginRequest request);

    @GetMapping("/resource")
    @Operation(summary = "Protected resource")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<JwtUsernameResponse> getProtected(
            @RequestHeader("Authorization") String authorizationHeader
    );
    @DeleteMapping("/reset")
    @Operation(summary = "Reset JWT auth state", description = "Reset JWT users and tokens to default state")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<Void> reset();
}