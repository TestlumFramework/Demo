package com.knubisoft.testapi.repository.oracle;

import com.knubisoft.testapi.model.oracle.OracleNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OracleNewsRepo extends JpaRepository<OracleNews, Long> {

    List<OracleNews> findByName(String name);
}
