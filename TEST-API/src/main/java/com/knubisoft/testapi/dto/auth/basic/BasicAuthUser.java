package com.knubisoft.testapi.dto.auth.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicAuthUser {
    private String username;
    private String password;
    private String role;
}