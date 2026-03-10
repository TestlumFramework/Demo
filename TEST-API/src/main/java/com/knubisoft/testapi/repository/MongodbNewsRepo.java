package com.knubisoft.testapi.repository;

import com.knubisoft.testapi.model.MongoNews;

import java.util.List;

public interface MongodbNewsRepo {

    MongoNews findById(String id);

    List<MongoNews> findByName(String name);

    MongoNews save(MongoNews news);

    List<MongoNews> findAll();
}
