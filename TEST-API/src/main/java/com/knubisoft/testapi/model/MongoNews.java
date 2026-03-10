package com.knubisoft.testapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mongo_news")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoNews extends Entity {
}
