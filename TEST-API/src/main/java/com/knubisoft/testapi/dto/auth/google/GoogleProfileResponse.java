package com.knubisoft.testapi.dto.auth.google;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleProfileResponse {

    private final String name;
    private final String email;
}
