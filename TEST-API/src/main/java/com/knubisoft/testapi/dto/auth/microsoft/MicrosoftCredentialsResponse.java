package com.knubisoft.testapi.dto.auth.microsoft;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MicrosoftCredentialsResponse {

    private final String refreshToken;
    private final String accessToken;
    private final String tokenType;
    private final long expiresIn;
}
