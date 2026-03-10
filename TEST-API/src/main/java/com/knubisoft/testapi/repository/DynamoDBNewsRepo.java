package com.knubisoft.testapi.repository;

import com.knubisoft.testapi.model.DynamoNews;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface DynamoDBNewsRepo extends CrudRepository<DynamoNews, String> {

    List<DynamoNews> findByName(String name);

    List<DynamoNews> findAll();
}
