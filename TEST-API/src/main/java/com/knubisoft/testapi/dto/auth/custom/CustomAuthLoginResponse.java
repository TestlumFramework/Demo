package com.knubisoft.testapi.dto.auth.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomAuthLoginResponse {
    private String token;
}