package com.knubisoft.testapi.api.impl;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
@GraphQLApi
public class GraphqlApiMock {
    @GraphQLQuery(name = "idMock", description = "IDMock")
    private String idMock;
}
