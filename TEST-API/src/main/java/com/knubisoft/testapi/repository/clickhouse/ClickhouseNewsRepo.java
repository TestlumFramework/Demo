package com.knubisoft.testapi.repository.clickhouse;

import com.knubisoft.testapi.model.clickhouse.ClickhouseNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClickhouseNewsRepo extends JpaRepository<ClickhouseNews, Long> {

    List<ClickhouseNews> findByName(String name);
}
