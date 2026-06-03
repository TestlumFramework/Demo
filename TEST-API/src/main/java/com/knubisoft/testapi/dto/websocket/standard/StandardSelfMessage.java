package com.knubisoft.testapi.dto.websocket.standard;

import lombok.Data;

@Data
public class StandardSelfMessage {
    private String action;
    private String id;
    private String value;
}