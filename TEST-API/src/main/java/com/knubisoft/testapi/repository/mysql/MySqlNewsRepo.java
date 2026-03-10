package com.knubisoft.testapi.repository.mysql;

import com.knubisoft.testapi.model.mysql.MysqlNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MySqlNewsRepo extends JpaRepository<MysqlNews, Long> {

    List<MysqlNews> findByName(String name);
}
