package com.knubisoft.testapi.model;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "news")
public class ESNews extends Entity {

}
