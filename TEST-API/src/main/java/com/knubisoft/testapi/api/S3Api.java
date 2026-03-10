package com.knubisoft.testapi.api;

import com.knubisoft.testapi.dto.FileDetailsDto;
import com.knubisoft.testapi.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ApiResponses(value = {
        @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(
                schema = @Schema(implementation = ErrorResponse.class))})
})
@RequestMapping(value = "/s3")
public interface S3Api {

    @PostMapping("/buckets")
    @Operation(summary = "Create bucket", description = "Create bucket")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.TEXT_PLAIN_VALUE,
            schema = @Schema(description = "Returns string with created bucket name"))})
    ResponseEntity<String> createBucket(
            @Parameter(description = "Bucket name for create", required = true)
            @RequestParam("bucketName") final String bucketName);

    @DeleteMapping("/buckets")
    @Operation(summary = "Delete bucket", description = "Delete bucket")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.TEXT_PLAIN_VALUE,
            schema = @Schema(description = "Returns string with deleted bucket name"))})
    ResponseEntity<String> deleteBucket(
            @Parameter(description = "Bucket name for remove", required = true)
            @RequestParam("bucketName") final String bucketName);

    @GetMapping("/buckets")
    @Operation(summary = "List bucket", description = "List bucket")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(description = "Returns list of buckets"))})
    ResponseEntity<List<String>> listAllBuckets();

    @PostMapping("/{bucketName}/upload")
    @Operation(summary = "Upload file", description = "Upload file to bucket")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.TEXT_PLAIN_VALUE,
            schema = @Schema(description = "Returns string of uploaded file name"))})
    ResponseEntity<String> upload(
            @Parameter(description = "Bucket name for call", required = true)
            @PathVariable final String bucketName,
            @Parameter(description = "File name to upload", required = true)
            @RequestPart final MultipartFile file);

    @GetMapping("/{bucketName}/{fileName}")
    @Operation(summary = "Output details", description = "Output file details")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(description = "Returns strings of file details"))})
    ResponseEntity<FileDetailsDto> outputDetails(
            @Parameter(description = "Bucket name for call", required = true)
            @PathVariable final String bucketName,
            @Parameter(description = "File name for details output", required = true)
            @PathVariable final String fileName);

    @GetMapping("/{bucketName}/{fileName}/download")
    @Operation(summary = "Download file", description = "Download file from bucket")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            schema = @Schema(description = "Returns file"))})
    ResponseEntity<InputStreamResource> download(
            @Parameter(description = "Bucket name for call", required = true)
            @PathVariable final String bucketName,
            @Parameter(description = "File name for download", required = true)
            @PathVariable final String fileName);

    @DeleteMapping("/{bucketName}/{fileName}")
    @Operation(summary = "Remove file", description = "Remove file from bucket")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.TEXT_PLAIN_VALUE,
            schema = @Schema(description = "Returns removed name of file"))})
    ResponseEntity<String> remove(
            @Parameter(description = "Bucket name for call", required = true)
            @PathVariable final String bucketName,
            @Parameter(description = "File name for remove", required = true)
            @PathVariable final String fileName);

    @GetMapping("/{bucketName}/files")
    @Operation(summary = "List all files", description = "List all files' names in bucket")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(description = "Returns list of files' names"))})
    ResponseEntity<List<String>> listAll(
            @Parameter(description = "Bucket name for listing all contained files", required = true)
            @PathVariable final String bucketName);

    @DeleteMapping("/{bucketName}/files")
    @Operation(summary = "Remove all files", description = "Remove all files from bucket")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
            mediaType = MediaType.TEXT_PLAIN_VALUE,
            schema = @Schema(description = "Return string"))})
    ResponseEntity<String> removeAll(
            @Parameter(description = "Bucket name for removing all contained files", required = true)
            @PathVariable final String bucketName);
}
