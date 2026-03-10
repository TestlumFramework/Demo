package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.MySqlNewsApi;
import com.knubisoft.testapi.dto.NewsDto;
import com.knubisoft.testapi.exception.ResourceNotFoundException;
import com.knubisoft.testapi.model.mysql.MysqlNews;
import com.knubisoft.testapi.repository.mysql.MySqlNewsRepo;
import com.knubisoft.testapi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@ConditionalOnExpression("${mysql.enabled:true}")
public class MySqlController implements MySqlNewsApi {

    private final MySqlNewsRepo mySqlNewsRepo;

    public ResponseEntity<MysqlNews> getNews(Long id) {
        MysqlNews mysqlNews = mySqlNewsRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MysqlNews.class, id));
        return ResponseEntity.ok(mysqlNews);
    }

    public ResponseEntity<List<MysqlNews>> getNewsByName(String name) {
        List<MysqlNews> mysqlNews = mySqlNewsRepo.findByName(name);
        return ResponseEntity.ok(mysqlNews);
    }

    public ResponseEntity<?> createNews(NewsDto dto) {
        MysqlNews mysqlNews = MapperUtil.convertFrom(dto, new MysqlNews());
        MysqlNews savedMysqlNews = mySqlNewsRepo.save(mysqlNews);
        return ResponseEntity.ok(savedMysqlNews);
    }

    public ResponseEntity<List<MysqlNews>> getAllNews() {
        return ResponseEntity.ok(mySqlNewsRepo.findAll());
    }
}
