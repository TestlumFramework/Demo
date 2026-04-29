package com.knubisoft.testapi.dto.websocket.standard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardSelfResponse {
    private String status;
    private String id;
    private String value;
}