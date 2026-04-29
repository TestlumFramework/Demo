package com.knubisoft.testapi.dto.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtLoginResponse {
    private String token;
}