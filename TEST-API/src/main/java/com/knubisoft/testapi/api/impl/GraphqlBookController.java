package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.GraphqlBookApi;
import com.knubisoft.testapi.model.MongoBook;
import com.knubisoft.testapi.repository.MongoGraphqlBooksRepo;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@GraphQLApi
@RequiredArgsConstructor
@ConditionalOnExpression("${graphql.mongodb.enabled:true}")
public class GraphqlBookController implements GraphqlBookApi {

    @Autowired
    private final MongoGraphqlBooksRepo mongoGraphqlBooksRepo;

    @GraphQLQuery(name = "getBookByTitle")
    public MongoBook getBookByTitle(@GraphQLArgument(name = "title") String title) {
        return mongoGraphqlBooksRepo.findByTitle(title);
    }

    @GraphQLQuery(name = "getAllBooks", description = "Get all books")
    public List<MongoBook> getAllBooks() {
        return mongoGraphqlBooksRepo.findAll();
    }

    @GraphQLMutation(name = "addBook")
    public MongoBook addBook(@GraphQLArgument(name = "newBook") MongoBook mongoBook) {
        mongoGraphqlBooksRepo.save(mongoBook);
        return mongoBook;
    }
}
