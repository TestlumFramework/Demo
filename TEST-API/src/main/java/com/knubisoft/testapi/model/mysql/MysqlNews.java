package com.knubisoft.testapi.model.mysql;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "news")
public class MysqlNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "newsName")
    private String name;
    @Column(name = "newsNumber")
    private Integer number;
    private boolean active;
    private LocalDateTime createdAt;
}
