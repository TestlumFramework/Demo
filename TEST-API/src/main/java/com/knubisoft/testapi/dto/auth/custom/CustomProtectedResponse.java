package com.knubisoft.testapi.dto.auth.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomProtectedResponse {
    private String message;
    private String authType;
}