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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ApiTestEntityController implements ApiTestEntityApi {

    private final Map<Long, ApiTestEntity> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public ResponseEntity<ApiTestEntity> create(final ApiTestEntityDto dto) {
        final Long id = idGenerator.getAndIncrement();

        final ApiTestEntity entity = new ApiTestEntity();
        entity.setId(id);
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setCategory(dto.getCategory());

        storage.put(id, entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @Override
    public ResponseEntity<ApiTestEntity> getById(final Long id) {
        return ResponseEntity.ok(getExistingEntity(id));
    }

    @Override
    public ResponseEntity<List<ApiTestEntity>> getAll() {
        final List<ApiTestEntity> entities = new ArrayList<>(storage.values());
        entities.sort(Comparator.comparing(ApiTestEntity::getId));
        return ResponseEntity.ok(entities);
    }

    @Override
    public ResponseEntity<ApiTestEntity> update(final Long id, final ApiTestEntityDto dto) {
        final ApiTestEntity entity = getExistingEntity(id);

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setCategory(dto.getCategory());

        return ResponseEntity.ok(entity);
    }

    @Override
    public ResponseEntity<ApiTestEntity> patch(final Long id, final ApiTestEntityPatchDto dto) {
        final ApiTestEntity entity = getExistingEntity(id);

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
    public ResponseEntity<Void> delete(final Long id) {
        getExistingEntity(id);
        storage.remove(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteAll() {
        storage.clear();
        idGenerator.set(1);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> head(final Long id) {
        final ApiTestEntity entity = getExistingEntity(id);

        return ResponseEntity.ok()
                .header("X-Entity-Id", String.valueOf(entity.getId()))
                .header("X-Entity-Name", entity.getName() == null ? "" : entity.getName())
                .header("X-Entity-Status", entity.getStatus() == null ? "" : entity.getStatus())
                .header("X-Entity-Category", entity.getCategory() == null ? "" : entity.getCategory())
                .build();
    }

    @Override
    public ResponseEntity<Void> options(final Long id) {
        getExistingEntity(id);

        return ResponseEntity.ok()
                .allow(
                        HttpMethod.GET,
                        HttpMethod.POST,
                        HttpMethod.PUT,
                        HttpMethod.PATCH,
                        HttpMethod.DELETE,
                        HttpMethod.HEAD,
                        HttpMethod.OPTIONS,
                        HttpMethod.TRACE
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
        final Long id = idGenerator.getAndIncrement();

        final ApiTestEntity entity = new ApiTestEntity();
        entity.setId(id);
        entity.setName(value);
        entity.setDescription("Created from params");
        entity.setStatus("PARAM");
        entity.setCategory("REQUEST_PARAM");

        storage.put(id, entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @Override
    public ResponseEntity<ApiTestEntity> enrichWithMultipart(final Long id,
                                                             final String text,
                                                             final MultipartFile file) throws IOException {
        final ApiTestEntity entity = getExistingEntity(id);
        entity.setDescription("Updated with multipart body");
        entity.setStatus("MULTIPART");
        entity.setCategory("FORM");
        if (text != null) {
            entity.setName(text);
        }
        if (file != null) {
            entity.setUploadedFile(new UploadedFileInfo(
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType()
            ));
        }

        return ResponseEntity.ok(entity);
    }

    @Override
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @Override
    public ResponseEntity<HeaderValidationResponse> validateHeaders(final String requestId,
                                                                    final String client,
                                                                    final String mode) {
        if (requestId == null || requestId.trim().isEmpty()) {
            throw new ApiTestEntityException("Missing required header: X-Test-Request-Id");
        }
        if (client == null || client.trim().isEmpty()) {
            throw new ApiTestEntityException("Missing required header: X-Test-Client");
        }
        if (mode == null || mode.trim().isEmpty()) {
            throw new ApiTestEntityException("Missing required header: X-Test-Mode");
        }
        return ResponseEntity.ok()
                .header("X-Validated-Request-Id", requestId)
                .header("X-Validated-Client", client)
                .header("X-Validated-Mode", mode)
                .header("X-Header-Validation", "SUCCESS")
                .body(new HeaderValidationResponse("Headers validated successfully"));
    }

    private ApiTestEntity getExistingEntity(final Long id) {
        final ApiTestEntity entity = storage.get(id);
        if (entity == null) {
            throw new ApiTestEntityException("ApiTestEntity with id " + id + " not found");
        }
        return entity;
    }
}