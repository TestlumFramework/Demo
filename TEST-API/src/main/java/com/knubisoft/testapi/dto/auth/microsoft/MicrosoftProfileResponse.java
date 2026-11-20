package com.knubisoft.testapi.dto.auth.microsoft;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MicrosoftProfileResponse {

    private final String name;
    private final String email;
}
