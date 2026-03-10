package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.LambdaFunctionApi;
import com.knubisoft.testapi.model.MongoBook;
import com.knubisoft.testapi.repository.MongoGraphqlBooksRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@ConditionalOnExpression("${lambda.mongo.enabled:true}")
public class LambdaFunctionController implements LambdaFunctionApi {

    private final MongoGraphqlBooksRepo mongoGraphqlBooksRepo;

    public ResponseEntity<MongoBook> getBookByTitle(String title) {
        MongoBook mongoBook = mongoGraphqlBooksRepo.findByTitle(title);
        return ResponseEntity.ok(mongoBook);
    }

    public ResponseEntity<List<MongoBook>> getAllBooks() {
        return ResponseEntity.ok(mongoGraphqlBooksRepo.findAll());
    }

    public ResponseEntity<MongoBook> addMongoBook(MongoBook mongoBook) {
        MongoBook savedMongoBook = mongoGraphqlBooksRepo.save(mongoBook);
        return ResponseEntity.ok(savedMongoBook);
    }
}
