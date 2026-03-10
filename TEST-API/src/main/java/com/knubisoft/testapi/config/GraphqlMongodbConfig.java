package com.knubisoft.testapi.config;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@ConditionalOnExpression("${graphql.mongodb.enabled:true} or ${lambda.mongo.enabled:true}")
public class GraphqlMongodbConfig extends MongodbConfig {

    @Value("${spring.data.mongodb.database.graphql}")
    private String graphqlMongoDatabase;

    @Bean("graphqlMongoTemplate")
    public MongoTemplate graphqlMongoTemplate() {
        return new MongoTemplate(getMongoClient(), graphqlMongoDatabase);
    }

    private MongoClient getMongoClient() {
        return getMongoClient(graphqlMongoDatabase);
    }
}
