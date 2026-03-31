package com.knubisoft.testapi.dto.auth.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicLoginResponse {
    private String message;
    private String authType;
    private String username;
    private String role;
    private String authorization;
}