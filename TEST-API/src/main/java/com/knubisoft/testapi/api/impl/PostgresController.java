package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.PostgresNewsApi;
import com.knubisoft.testapi.dto.NewsDto;
import com.knubisoft.testapi.exception.ResourceNotFoundException;
import com.knubisoft.testapi.model.postgres.PostgresNews;
import com.knubisoft.testapi.repository.postgres.PostgresNewsRepo;
import com.knubisoft.testapi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@ConditionalOnExpression("${postgres.enabled:true}")
public class PostgresController implements PostgresNewsApi {

    private final PostgresNewsRepo postgresNewsRepo;

    public ResponseEntity<PostgresNews> getNews(Long id) {
        PostgresNews news = postgresNewsRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PostgresNews.class, id));
        return ResponseEntity.ok(news);
    }

    public ResponseEntity<List<PostgresNews>> getNewsByName(String name) {
        List<PostgresNews> news = postgresNewsRepo.findByName(name);
        return ResponseEntity.ok(news);
    }

    public ResponseEntity<?> createNews(NewsDto dto) {
        PostgresNews news = MapperUtil.convertFrom(dto, new PostgresNews());
        PostgresNews savedNews = postgresNewsRepo.save(news);
        return ResponseEntity.ok(savedNews);
    }

    public ResponseEntity<List<PostgresNews>> getAllNews() {
        return ResponseEntity.ok(postgresNewsRepo.findAll());
    }
}
