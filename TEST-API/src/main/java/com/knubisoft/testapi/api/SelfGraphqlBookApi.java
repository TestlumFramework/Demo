package com.knubisoft.testapi.api;

import com.knubisoft.testapi.model.graphql.SelfGraphqlBook;

import java.util.List;

public interface SelfGraphqlBookApi {

    SelfGraphqlBook getSelfBookByTitle(String title);

    List<SelfGraphqlBook> getAllSelfBooks();

    SelfGraphqlBook addSelfBook(SelfGraphqlBook book);

    SelfGraphqlBook updateSelfBookByTitle(String title, SelfGraphqlBook updatedBook);

    boolean deleteSelfBookByTitle(String title);

    boolean resetSelfBooks();
}