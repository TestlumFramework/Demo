package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.MongodbNewsApi;
import com.knubisoft.testapi.dto.NewsDto;
import com.knubisoft.testapi.model.MongoNews;
import com.knubisoft.testapi.repository.MongodbNewsRepo;
import com.knubisoft.testapi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@ConditionalOnExpression("${mongodb.enabled:true}")
public class MongodbNewsController implements MongodbNewsApi {

    @Autowired
    private final MongodbNewsRepo mongodbNewsRepo;

    public ResponseEntity<MongoNews> getNews(String id) {
        MongoNews news = mongodbNewsRepo.findById(id);
        return ResponseEntity.ok(news);
    }

    public ResponseEntity<List<MongoNews>> getNewsByName(String name) {
        List<MongoNews> news = mongodbNewsRepo.findByName(name);
        news.sort(Comparator.comparing(MongoNews::getName));
        return ResponseEntity.ok(news);
    }

    public ResponseEntity<MongoNews> createNews(NewsDto dto) {
        MongoNews news = MapperUtil.convertFrom(dto, new MongoNews());
        MongoNews savedNews = mongodbNewsRepo.save(news);
        return ResponseEntity.ok(savedNews);
    }

    public ResponseEntity<List<MongoNews>> getAllNews() {
        List<MongoNews> newsList = mongodbNewsRepo.findAll();
        newsList.sort(Comparator.comparing(MongoNews::getName));
        return ResponseEntity.ok(newsList);
    }
}
