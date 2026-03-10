package com.knubisoft.testapi.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;

@Configuration
@ConditionalOnExpression("${mongodb.enabled:true}")
public class MongodbConfig {

    @Value("${spring.data.mongodb.host}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port}")
    private int mongoPort;

    @Value("${spring.data.mongodb.database}")
    private String mongoDatabase;

    @Value("${spring.data.mongodb.username}")
    private String mongoUsername;

    @Value("${spring.data.mongodb.password}")
    private String mongoPassword;

    @Bean("mongodbTemplate")
    public MongoTemplate mongodbTemplate() {
        return new MongoTemplate(getMongoClient(mongoDatabase), mongoDatabase);
    }

    protected MongoClient getMongoClient(String databaseName) {
        ServerAddress mongoAddress = new ServerAddress(mongoHost, mongoPort);
        MongoCredential mongoCredential = MongoCredential.createCredential(
                mongoUsername, databaseName, mongoPassword.toCharArray());

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(mongoAddress)))
                .credential(mongoCredential)
                .build();

        return MongoClients.create(mongoClientSettings);
    }
}
