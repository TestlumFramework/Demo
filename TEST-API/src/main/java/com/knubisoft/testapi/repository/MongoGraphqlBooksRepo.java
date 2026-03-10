package com.knubisoft.testapi.repository;

import com.knubisoft.testapi.model.MongoBook;

import java.util.List;

public interface MongoGraphqlBooksRepo {

    MongoBook findByTitle(String name);

    MongoBook save(MongoBook book);

    List<MongoBook> findAll();
}
