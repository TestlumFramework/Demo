package com.knubisoft.testapi.model.clickhouse;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(schema = "test_db", name = "news")
public class ClickhouseNews {
    @Id
    private Long id;
    @Column(name = "newsName")
    private String name;
    @Column(name = "newsNumber")
    private Integer number;
    private boolean active;
    private LocalDateTime createdAt;
}
