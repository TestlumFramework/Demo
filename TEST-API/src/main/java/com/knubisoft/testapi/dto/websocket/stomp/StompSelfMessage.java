package com.knubisoft.testapi.dto.websocket.stomp;

import lombok.Data;

@Data
public class StompSelfMessage {

    private String id;
    private String value;
}