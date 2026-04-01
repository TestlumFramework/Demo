package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.ApiTestEntityApi;
import com.knubisoft.testapi.dto.ApiTestEntityDto;
import com.knubisoft.testapi.dto.ApiTestEntityPatchDto;
import com.knubisoft.testapi.dto.HeaderValidationResponse;
import com.knubisoft.testapi.exception.ApiTestEntityException;
import com.knubisoft.testapi.model.http.ApiTestEntity;
import com.knubisoft.testapi.model.http.UploadedFileInfo;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class ApiTestEntityController implements ApiTestEntityApi {

    private final List<ApiTestEntity> storage = new CopyOnWriteArrayList<>();

    @Override
    public ResponseEntity<ApiTestEntity> create(final ApiTestEntityDto dto) {
        final ApiTestEntity entity = new ApiTestEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setCategory(dto.getCategory());

        storage.add(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @Override
    public ResponseEntity<List<ApiTestEntity>> getAll() {
        return ResponseEntity.ok(new ArrayList<>(storage));
    }

    @Override
    public ResponseEntity<ApiTestEntity> updateLast(final ApiTestEntityDto dto) {
        final ApiTestEntity entity = getLastEntity();

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setCategory(dto.getCategory());

        return ResponseEntity.ok(entity);
    }

    @Override
    public ResponseEntity<ApiTestEntity> patchLast(final ApiTestEntityPatchDto dto) {
        final ApiTestEntity entity = getLastEntity();

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getCategory() != null) {
            entity.setCategory(dto.getCategory());
        }

        return ResponseEntity.ok(entity);
    }

    @Override
    public ResponseEntity<Void> deleteAll() {
        storage.clear();
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> head() {
        final ApiTestEntity entity = getLastEntity();

        return ResponseEntity.ok()
                .header("X-Entity-Name", safe(entity.getName()))
                .header("X-Entity-Status", safe(entity.getStatus()))
                .header("X-Entity-Category", safe(entity.getCategory()))
                .build();
    }

    @Override
    public ResponseEntity<Void> options() {
        return ResponseEntity.ok()
                .allow(
                        HttpMethod.GET,
                        HttpMethod.POST,
                        HttpMethod.PUT,
                        HttpMethod.PATCH,
                        HttpMethod.DELETE,
                        HttpMethod.HEAD,
                        HttpMethod.OPTIONS
                )
                .build();
    }

    @Override
    public ResponseEntity<String> traceLike(final String body) {
        final String response = "TRACE /api/test/entities/trace\n"
                + "Content-Type: text/plain\n"
                + "Body: " + body;

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiTestEntity> createFromParams(final String value) {
        final ApiTestEntity entity = new ApiTestEntity();
        entity.setName(value);
        entity.setDescription("Created from params");
        entity.setStatus("PARAM");
        entity.setCategory("REQUEST_PARAM");

        storage.add(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @Override
    public ResponseEntity<ApiTestEntity> createWithMultipart(final String text,
                                                             final MultipartFile file) throws IOException {
        final ApiTestEntity entity = new ApiTestEntity();
        entity.setName(text != null ? text : "Multipart Entity");
        entity.setDescription("Created with multipart body");
        entity.setStatus("MULTIPART");
        entity.setCategory("FORM");

        if (file != null) {
            entity.setUploadedFile(new UploadedFileInfo(
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType()
            ));
        }

        storage.add(entity);
        return ResponseEntity.ok(entity);
    }

    @Override
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @Override
    public ResponseEntity<HeaderValidationResponse> validateHeaders(final String testHeader) {
        if (testHeader == null || testHeader.trim().isEmpty()) {
            throw new ApiTestEntityException("Missing required header: X-Test-Header");
        }

        return ResponseEntity.ok()
                .header("X-Validated-Header", testHeader)
                .body(new HeaderValidationResponse("Header validated successfully"));
    }

    @Override
    public ResponseEntity<Void> reset() {
        storage.clear();
        return ResponseEntity.ok().build();
    }

    private ApiTestEntity getLastEntity() {
        if (storage.isEmpty()) {
            throw new ApiTestEntityException("No entities found");
        }
        return storage.get(storage.size() - 1);
    }

    private String safe(final String value) {
        return value == null ? "" : value;
    }
}