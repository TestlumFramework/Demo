package com.knubisoft.testapi.api;

import com.knubisoft.testapi.dto.auth.custom.CustomAuthLoginRequest;
import com.knubisoft.testapi.dto.auth.custom.CustomAuthLoginResponse;
import com.knubisoft.testapi.dto.auth.custom.CustomProtectedResponse;
import com.knubisoft.testapi.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                @Content(schema = @Schema(implementation = ErrorResponse.class))
        }),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                @Content(schema = @Schema(implementation = ErrorResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                @Content(schema = @Schema(implementation = ErrorResponse.class))
        })
})
@RequestMapping("/api/test/auth/custom")
public interface ApiTestCustomAuthApi {

    @PostMapping("/login")
    @Operation(summary = "Custom auth login", description = "Authenticate and return custom token")
    @ApiResponse(responseCode = "200", description = "Success", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CustomAuthLoginResponse.class))
    })
    ResponseEntity<CustomAuthLoginResponse> login(@RequestBody CustomAuthLoginRequest request);

    @GetMapping("/resource")
    @Operation(summary = "Protected custom auth resource", description = "Access protected endpoint with custom token")
    @ApiResponse(responseCode = "200", description = "Success", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CustomProtectedResponse.class))
    })
    ResponseEntity<CustomProtectedResponse> getProtectedResource(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    );

    @DeleteMapping("/reset")
    @Operation(summary = "Reset custom auth state", description = "Reset custom auth state after test")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<Void> reset();
}