package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.DynamoDBNewsApi;
import com.knubisoft.testapi.dto.NewsDto;
import com.knubisoft.testapi.exception.ResourceNotFoundException;
import com.knubisoft.testapi.model.DynamoNews;
import com.knubisoft.testapi.repository.DynamoDBNewsRepo;
import com.knubisoft.testapi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@ConditionalOnExpression("${dynamodb.enabled:true}")
public class DynamoDBNewsController implements DynamoDBNewsApi {

    @Autowired
    private final DynamoDBNewsRepo dynamoDBNewsRepo;

    public ResponseEntity<DynamoNews> getNews(String id) {
        DynamoNews news = dynamoDBNewsRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DynamoNews.class, Long.parseLong(id)));
        return ResponseEntity.ok(news);
    }

    public ResponseEntity<List<DynamoNews>> getNewsByName(String name) {
        List<DynamoNews> newsList = dynamoDBNewsRepo.findByName(name).stream()
                .sorted(Comparator.comparing(DynamoNews::getName))
                .collect(Collectors.toList());
        return ResponseEntity.ok(newsList);
    }

    public ResponseEntity<?> createNews(NewsDto dto) {
        DynamoNews news = MapperUtil.convertFrom(dto, new DynamoNews());
        DynamoNews savedNews = dynamoDBNewsRepo.save(news);
        return ResponseEntity.ok(savedNews);
    }

    public ResponseEntity<List<DynamoNews>> getAllNews() {
        List<DynamoNews> newsList = dynamoDBNewsRepo.findAll().stream()
                .sorted(Comparator.comparing(DynamoNews::getNumber))
                .collect(Collectors.toList());
        return ResponseEntity.ok(newsList);
    }
}
