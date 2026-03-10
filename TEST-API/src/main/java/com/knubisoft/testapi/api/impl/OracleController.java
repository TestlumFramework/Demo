package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.OracleNewsApi;
import com.knubisoft.testapi.dto.NewsDto;
import com.knubisoft.testapi.exception.ResourceNotFoundException;
import com.knubisoft.testapi.model.oracle.OracleNews;
import com.knubisoft.testapi.repository.oracle.OracleNewsRepo;
import com.knubisoft.testapi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@ConditionalOnExpression("${oracle.enabled:true}")
public class OracleController implements OracleNewsApi {

    private final OracleNewsRepo oracleNewsRepo;

    @GetMapping("/news/{id}")
    public ResponseEntity<OracleNews> getNews(Long id) {
        OracleNews news = oracleNewsRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(OracleNews.class, id));
        return ResponseEntity.ok(news);
    }

    @GetMapping("/news/by")
    public ResponseEntity<List<OracleNews>> getNewsByName(String name) {
        List<OracleNews> news = oracleNewsRepo.findByName(name);
        return ResponseEntity.ok(news);
    }

    @PostMapping("/news")
    public ResponseEntity<?> createNews(NewsDto dto) {
        OracleNews news = MapperUtil.convertFrom(dto, new OracleNews());
        OracleNews savedNews = oracleNewsRepo.save(news);
        return ResponseEntity.ok(savedNews);
    }

    @GetMapping("/news")
    public ResponseEntity<List<OracleNews>> getAllNews() {
        return ResponseEntity.ok(oracleNewsRepo.findAll());
    }
}
