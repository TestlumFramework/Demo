package com.knubisoft.testapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
public class UserDto {

    @NotBlank(message = "Username is required")
    @Length(min = 4)
    @Schema(description = "User name", required = true)
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "User's password", required = true)
    private String password;

    @NotBlank(message = "Role is required")
    @Length(min = 4)
    @Schema(description = "User's role", required = true, allowableValues = {"USER", "ADMIN"})
    private String role;

    @Schema(description = "State of users account", required = true)
    private boolean isAccountNonExpired;

    @Schema(description = "State of users account", required = true)
    private boolean isAccountNonLocked;

    @Schema(description = "State of users account", required = true)
    private boolean isCredentialsNonExpired;

    @Schema(description = "State of users account", required = true)
    private boolean isEnabled;
}
