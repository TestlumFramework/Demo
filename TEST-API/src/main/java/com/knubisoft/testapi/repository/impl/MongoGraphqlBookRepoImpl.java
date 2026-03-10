package com.knubisoft.testapi.repository.impl;

import com.knubisoft.testapi.exception.ResourceNotFoundException;
import com.knubisoft.testapi.model.MongoBook;
import com.knubisoft.testapi.repository.MongoGraphqlBooksRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@ConditionalOnExpression("${graphql.mongodb.enabled:true} or ${lambda.mongo.enabled:true}")
public class MongoGraphqlBookRepoImpl implements MongoGraphqlBooksRepo {

    private final MongoTemplate graphqlMongoTemplate;

    public MongoGraphqlBookRepoImpl(@Qualifier("graphqlMongoTemplate") MongoTemplate graphqlMongoTemplate) {
        this.graphqlMongoTemplate = graphqlMongoTemplate;
    }

    public MongoBook findByTitle(String title) {
        Criteria criteria = Criteria.where("title").is(title);
        MongoBook mongoBook = graphqlMongoTemplate.findOne(Query.query(criteria), MongoBook.class);
        if (Objects.nonNull(mongoBook)) {
            return mongoBook;
        }
        throw new ResourceNotFoundException(MongoBook.class, title);
    }

    public MongoBook save(MongoBook book) {
        return graphqlMongoTemplate.save(book);
    }

    public List<MongoBook> findAll() {
        return graphqlMongoTemplate.findAll(MongoBook.class);
    }
}
