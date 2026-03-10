package com.knubisoft.testapi.api;

import com.knubisoft.testapi.model.MongoBook;

import java.util.List;

public interface GraphqlBookApi {

    MongoBook getBookByTitle(String title);

    MongoBook addBook(MongoBook mongoBook);

    List<MongoBook> getAllBooks();
}
