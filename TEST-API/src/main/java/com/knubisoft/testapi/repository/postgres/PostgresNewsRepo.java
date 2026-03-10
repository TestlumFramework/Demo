package com.knubisoft.testapi.repository.postgres;

import com.knubisoft.testapi.model.postgres.PostgresNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostgresNewsRepo extends JpaRepository<PostgresNews, Long> {

    List<PostgresNews> findByName(String name);
}
