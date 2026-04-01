package com.knubisoft.testapi.model.graphql;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SelfGraphqlBook {
    private String title;
    private String author;
    private Integer number;
}