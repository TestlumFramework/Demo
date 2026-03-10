package com.knubisoft.testapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "mongo_book")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoBook {
    @Id
    private String id;
    private String author;
    private String title;
    private Integer number;
}
