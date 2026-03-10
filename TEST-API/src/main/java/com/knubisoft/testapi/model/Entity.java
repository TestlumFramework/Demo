package com.knubisoft.testapi.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
public class Entity {

    @Id
    protected String id;
    protected String name;
    protected int number;
    protected boolean active;
    protected LocalDateTime createdAt;
}
