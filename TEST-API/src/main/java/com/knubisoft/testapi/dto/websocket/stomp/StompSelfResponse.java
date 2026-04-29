package com.knubisoft.testapi.dto.websocket.stomp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StompSelfResponse {

    private String status;
    private String id;
    private String value;
}