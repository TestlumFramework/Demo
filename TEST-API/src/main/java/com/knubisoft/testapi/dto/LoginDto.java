package com.knubisoft.testapi.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Validated
public class LoginDto {

    @NotNull(message = "Username is required")
    private String username;

    @Length(min = 4)
    @NotNull(message = "Password is required")
    private String password;
}
