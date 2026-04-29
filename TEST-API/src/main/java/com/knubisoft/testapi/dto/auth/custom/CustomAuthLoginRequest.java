package com.knubisoft.testapi.dto.auth.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomAuthLoginRequest {
    private String clientId;
    private String clientSecret;
}