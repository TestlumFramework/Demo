package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.ESNewsApi;
import com.knubisoft.testapi.dto.NewsDto;
import com.knubisoft.testapi.exception.ResourceNotFoundException;
import com.knubisoft.testapi.model.ESNews;
import com.knubisoft.testapi.repository.es.ESNewsRepo;
import com.knubisoft.testapi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@ConditionalOnExpression("${es.enabled:true}")
public class ESController implements ESNewsApi {

    @Autowired
    private final ESNewsRepo esNewsRepo;

    @Override
    public ResponseEntity<ESNews> getNews(String id) {
        return ResponseEntity.ok(esNewsRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ESNews.class, Long.parseLong(id))));
    }

    @Override
    public ResponseEntity<List<ESNews>> getNewsByName(String name) {
        List<ESNews> news = esNewsRepo.findByName(name);
        return ResponseEntity.ok(news);
    }

    @Override
    public ResponseEntity<ESNews> createNews(NewsDto dto) {
        ESNews news = MapperUtil.convertFrom(dto, new ESNews());
        ESNews savedNews = esNewsRepo.save(news);
        return ResponseEntity.ok(savedNews);
    }

    @Override
    public ResponseEntity<Iterable<ESNews>> getAllNews() {
        return ResponseEntity.ok(esNewsRepo.findAll());
    }
}
