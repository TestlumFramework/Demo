package com.knubisoft.testapi.dto.auth.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicLoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}