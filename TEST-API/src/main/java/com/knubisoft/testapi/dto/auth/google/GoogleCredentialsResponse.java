package com.knubisoft.testapi.dto.auth.google;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleCredentialsResponse {

    private final String refreshToken;
}
