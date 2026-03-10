package com.knubisoft.testapi.api;

import com.knubisoft.testapi.dto.AuthResponseDto;
import com.knubisoft.testapi.dto.LoginDto;
import com.knubisoft.testapi.dto.UserDto;
import com.knubisoft.testapi.exception.ErrorResponse;
import com.knubisoft.testapi.model.postgresUsers.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@ApiResponses(value = {
        @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))})
})
@RequestMapping("/jwt")
public interface JwtAuthApi {

    @PostMapping("/login")
    @Operation(summary = "Logging in with jwt auth", description = "User authentication")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AuthResponseDto.class))}
    )
    ResponseEntity<AuthResponseDto> jwtLogin(@Parameter(description = "Data for login", required = true)
                                             @RequestBody @Valid LoginDto loginDto);

    @PostMapping("/users")
    @Operation(summary = "Add a user", description = "Create user from requested data")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = User.class))}
    )
    ResponseEntity<User> createUser(@Parameter(description = "Data for user", required = true)
                                    @RequestBody @Valid UserDto dto);

    @GetMapping("/users")
    @Operation(summary = "Get list of all users", description = "Return list of all users")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = User.class)))}
    )
    ResponseEntity<List<User>> getAllUsers();
}
