package com.knubisoft.testapi.util;

import com.knubisoft.testapi.dto.NewsDto;
import com.knubisoft.testapi.dto.UserDto;
import com.knubisoft.testapi.model.Entity;
import com.knubisoft.testapi.model.mysql.MysqlNews;
import com.knubisoft.testapi.model.oracle.OracleNews;
import com.knubisoft.testapi.model.postgres.PostgresNews;
import com.knubisoft.testapi.model.postgresUsers.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MapperUtil {

    public <T extends Entity> T convertFrom(NewsDto dto, T entity) {
        entity.setName(dto.getName());
        entity.setNumber(dto.getNumber());
        entity.setActive(dto.isActive());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }

    public MysqlNews convertFrom(NewsDto dto, MysqlNews entity) {
        entity.setName(dto.getName());
        entity.setNumber(dto.getNumber());
        entity.setActive(dto.isActive());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }

    public OracleNews convertFrom(NewsDto dto, OracleNews entity) {
        entity.setName(dto.getName());
        entity.setNumber(dto.getNumber());
        entity.setActive(dto.isActive());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }

    public PostgresNews convertFrom(NewsDto dto, PostgresNews entity) {
        entity.setName(dto.getName());
        entity.setNumber(dto.getNumber());
        entity.setActive(dto.isActive());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }

    public User convertFrom(UserDto dto, User entity) {
        entity.setUsername(dto.getUsername());
        entity.setRole(dto.getRole());
        entity.setAccountNonExpired(dto.isAccountNonExpired());
        entity.setAccountNonLocked(dto.isAccountNonLocked());
        entity.setCredentialsNonExpired(dto.isCredentialsNonExpired());
        entity.setEnabled(dto.isEnabled());
        return entity;
    }
}
