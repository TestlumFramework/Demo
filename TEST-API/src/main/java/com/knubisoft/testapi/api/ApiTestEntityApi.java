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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
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

    @GetMapping("/{id}")
    @Operation(summary = "Get entity by id", description = "Get API test entity by id")
    @ApiResponse(responseCode = "200", description = "Success", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiTestEntity.class)
            )
    })
    ResponseEntity<ApiTestEntity> getById(@PathVariable Long id);

    @GetMapping
    @Operation(summary = "Get all entities", description = "Return all API test entities")
    @ApiResponse(responseCode = "200", description = "Success", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ApiTestEntity.class))
            )
    })
    ResponseEntity<List<ApiTestEntity>> getAll();

    @PutMapping("/{id}")
    @Operation(summary = "Update entity", description = "Full update of API test entity")
    @ApiResponse(responseCode = "200", description = "Success", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiTestEntity.class)
            )
    })
    ResponseEntity<ApiTestEntity> update(
            @PathVariable Long id,
            @RequestBody @Valid ApiTestEntityDto dto
    );

    @PatchMapping("/{id}")
    @Operation(summary = "Patch entity", description = "Partial update of API test entity")
    @ApiResponse(responseCode = "200", description = "Success", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiTestEntity.class)
            )
    })
    ResponseEntity<ApiTestEntity> patch(
            @PathVariable Long id,
            @RequestBody @Valid ApiTestEntityPatchDto dto
    );

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete entity", description = "Delete API test entity by id")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<Void> delete(@PathVariable Long id);

    @DeleteMapping
    @Operation(summary = "Delete all entities", description = "Delete all API test entities")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<Void> deleteAll();

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    @Operation(summary = "HEAD entity", description = "Return only headers for entity")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<Void> head(@PathVariable Long id);

    @RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
    @Operation(summary = "OPTIONS entity", description = "Return allowed methods")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<Void> options(@PathVariable Long id);

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

    @PostMapping(value = "/{id}/multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Enrich entity with multipart", description = "Attach multipart data to existing entity")
    @ApiResponse(responseCode = "200", description = "Updated", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiTestEntity.class)
            )
    })
    ResponseEntity<ApiTestEntity> enrichWithMultipart(
            @PathVariable Long id,
            @RequestPart(value = "text", required = false) String text,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException;

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Simple health endpoint")
    @ApiResponse(responseCode = "200", description = "Success")
    ResponseEntity<String> health();

    @PostMapping(value = "/headers", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Validate request headers", description = "Validate required request headers and return response headers")
    @ApiResponse(responseCode = "200", description = "Headers validated successfully", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = HeaderValidationResponse.class))
    })
    ResponseEntity<HeaderValidationResponse> validateHeaders(
            @RequestHeader(value = "X-Test-Request-Id") String requestId,
            @RequestHeader(value = "X-Test-Client") String client,
            @RequestHeader(value = "X-Test-Mode") String mode
    );
}