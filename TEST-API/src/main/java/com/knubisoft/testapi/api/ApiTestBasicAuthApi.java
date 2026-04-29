package com.knubisoft.testapi.api;

import com.knubisoft.testapi.dto.auth.basic.BasicCreateUserRequest;
import com.knubisoft.testapi.dto.auth.basic.BasicUserResponse;
import com.knubisoft.testapi.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import javax.validation.Valid;
import java.util.List;

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
@RequestMapping("/api/test/auth/basic")
public interface ApiTestBasicAuthApi {

    @GetMapping("/users")
    @Operation(summary = "Get all users with basic auth", description = "Protected endpoint that requires Authorization Basic header")
    @ApiResponse(responseCode = "200", description = "Success", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = BasicUserResponse.class))
            )
    })
    ResponseEntity<List<BasicUserResponse>> getUsers(
            @Parameter(description = "Authorization header with Basic credentials", required = true)
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    );

    @PostMapping("/users")
    @Operation(summary = "Create user with basic auth", description = "Protected endpoint that creates new user")
    @ApiResponse(responseCode = "200", description = "Success", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BasicUserResponse.class)
            )
    })
    ResponseEntity<BasicUserResponse> createUser(
            @Parameter(description = "Authorization header with Basic credentials", required = true)
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody @Valid BasicCreateUserRequest request
    );
    @DeleteMapping("/reset")
    @Operation(summary = "Reset basic auth state", description = "Reset users to default state")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<Void> reset();
}