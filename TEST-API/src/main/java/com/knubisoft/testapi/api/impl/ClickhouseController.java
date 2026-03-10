package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.ClickhouseNewsApi;
import com.knubisoft.testapi.dto.NewsDto;
import com.knubisoft.testapi.exception.ResourceNotFoundException;
import com.knubisoft.testapi.model.clickhouse.ClickhouseNews;
import com.knubisoft.testapi.repository.clickhouse.ClickhouseNewsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@ConditionalOnExpression("${clickhouse.enabled:true}")
public class ClickhouseController implements ClickhouseNewsApi {

    private final ClickhouseNewsRepo clickhouseNewsRepo;

    @GetMapping("/news/{id}")
    public ResponseEntity<ClickhouseNews> getNews(Long id) {
        ClickhouseNews news = clickhouseNewsRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ClickhouseNews.class, id));
        return ResponseEntity.ok(news);
    }

    @GetMapping("/news/by")
    public ResponseEntity<List<ClickhouseNews>> getNewsByName(String name) {
        List<ClickhouseNews> news = clickhouseNewsRepo.findByName(name);
        return ResponseEntity.ok(news);
    }

    @PostMapping("/news")
    public ResponseEntity<?> createNews(NewsDto dto) {
        ClickhouseNews news = new ClickhouseNews();
        news.setId(clickhouseNewsRepo.count() + 1L);
        news.setName(dto.getName());
        news.setNumber(dto.getNumber());
        news.setActive(dto.isActive());
        news.setCreatedAt(dto.getCreatedAt());
        ClickhouseNews savedNews = clickhouseNewsRepo.save(news);
        return ResponseEntity.ok(savedNews);
    }

    @GetMapping("/news")
    public ResponseEntity<List<ClickhouseNews>> getAllNews() {
        return ResponseEntity.ok(clickhouseNewsRepo.findAll());
    }
}
