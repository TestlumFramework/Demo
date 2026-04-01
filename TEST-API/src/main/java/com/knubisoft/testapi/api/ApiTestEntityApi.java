package com.knubisoft.testapi.api;

import com.knubisoft.testapi.dto.ApiTestEntityDto;
import com.knubisoft.testapi.dto.ApiTestEntityPatchDto;
import com.knubisoft.testapi.dto.HeaderValidationResponse;
import com.knubisoft.testapi.exception.ErrorResponse;
import com.knubisoft.testapi.model.http.ApiTestEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                @Content(schema = @Schema(implementation = ErrorResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Not Found", content = {
                @Content(schema = @Schema(implementation = ErrorResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                @Content(schema = @Schema(implementation = ErrorResponse.class))
        })
})
@RequestMapping("/api/test/entities")
public interface ApiTestEntityApi {

    @PostMapping
    @Operation(summary = "Create entity", description = "Create API test entity in memory")
    @ApiResponse(responseCode = "201", description = "Created", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiTestEntity.class)
            )
    })
    ResponseEntity<ApiTestEntity> create(
            @Parameter(description = "Entity request body", required = true)
            @RequestBody @Valid ApiTestEntityDto dto
    );

    @GetMapping
    @Operation(summary = "Get all entities", description = "Return all API test entities")
    @ApiResponse(responseCode = "200", description = "Success", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ApiTestEntity.class))
            )
    })
    ResponseEntity<List<ApiTestEntity>> getAll();

    @PutMapping
    @Operation(summary = "Replace last entity", description = "Replace last created entity")
    @ApiResponse(responseCode = "200", description = "Success", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiTestEntity.class)
            )
    })
    ResponseEntity<ApiTestEntity> updateLast(
            @RequestBody @Valid ApiTestEntityDto dto
    );

    @PatchMapping
    @Operation(summary = "Patch last entity", description = "Partially update last created entity")
    @ApiResponse(responseCode = "200", description = "Success", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiTestEntity.class)
            )
    })
    ResponseEntity<ApiTestEntity> patchLast(
            @RequestBody @Valid ApiTestEntityPatchDto dto
    );

    @DeleteMapping
    @Operation(summary = "Delete all entities", description = "Delete all API test entities")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<Void> deleteAll();

    @RequestMapping(method = RequestMethod.HEAD)
    @Operation(summary = "HEAD last entity", description = "Return only headers for last entity")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<Void> head();

    @RequestMapping(method = RequestMethod.OPTIONS)
    @Operation(summary = "OPTIONS entities", description = "Return allowed methods")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<Void> options();

    @PostMapping(value = "/trace", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "TRACE-like request", description = "Echo raw request body")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<String> traceLike(@RequestBody String body);

    @PostMapping("/params")
    @Operation(summary = "Create entity from params", description = "Create entity from request params")
    @ApiResponse(responseCode = "201", description = "Created", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiTestEntity.class)
            )
    })
    ResponseEntity<ApiTestEntity> createFromParams(@RequestParam("value") String value);

    @PostMapping(value = "/multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create entity with multipart", description = "Create entity using multipart form data")
    @ApiResponse(responseCode = "200", description = "Updated", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiTestEntity.class)
            )
    })
    ResponseEntity<ApiTestEntity> createWithMultipart(
            @RequestPart(value = "text", required = false) String text,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException;

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Simple health endpoint")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<String> health();

    @PostMapping(value = "/headers", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Validate single request header", description = "Validate one request header and return one response header")
    @ApiResponse(responseCode = "200", description = "Headers validated successfully", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = HeaderValidationResponse.class))
    })
    ResponseEntity<HeaderValidationResponse> validateHeaders(
            @RequestHeader(value = "X-Test-Header") String testHeader
    );

    @PostMapping("/reset")
    @Operation(summary = "Reset entities", description = "Reset all in-memory entities")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<Void> reset();
}