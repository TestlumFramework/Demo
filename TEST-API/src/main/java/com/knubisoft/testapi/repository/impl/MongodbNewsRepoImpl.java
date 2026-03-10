package com.knubisoft.testapi.repository.impl;

import com.knubisoft.testapi.exception.ResourceNotFoundException;
import com.knubisoft.testapi.model.MongoNews;
import com.knubisoft.testapi.repository.MongodbNewsRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ConditionalOnExpression("${mongodb.enabled:true}")
public class MongodbNewsRepoImpl implements MongodbNewsRepo {

    private final MongoTemplate mongodbTemplate;

    public MongodbNewsRepoImpl(@Qualifier("mongodbTemplate") MongoTemplate mongodbTemplate) {
        this.mongodbTemplate = mongodbTemplate;
    }

    public MongoNews findById(String id) {
        MongoNews news = mongodbTemplate.findById(id, MongoNews.class);
        if (news == null) {
            throw new ResourceNotFoundException(MongoNews.class, Long.parseLong(id));
        }
        return news;
    }

    public List<MongoNews> findByName(String name) {
        Criteria criteria = Criteria.where("name").is(name);
        return mongodbTemplate.find(Query.query(criteria), MongoNews.class);
    }

    public MongoNews save(MongoNews news) {
        return mongodbTemplate.insert(news);
    }

    public List<MongoNews> findAll() {
        return mongodbTemplate.findAll(MongoNews.class);
    }
}
