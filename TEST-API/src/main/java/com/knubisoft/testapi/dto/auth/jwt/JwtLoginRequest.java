package com.knubisoft.testapi.dto.auth.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtLoginRequest {
    private String username;
    private String password;
}