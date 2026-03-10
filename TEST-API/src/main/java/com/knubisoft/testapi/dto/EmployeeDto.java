package com.knubisoft.testapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Validated
@Getter
@Setter
@RedisHash("Employee")
public class EmployeeDto implements Serializable {

    @Id
    @Positive
    @NotNull(message = "Id is required")
    @Schema(pattern = "^[1-9]\\d*$", description = "Employee id (only positive numbers)", required = true)
    private int id;

    @NotBlank(message = "First name is required")
    @Schema(description = "First name employee", required = true)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "Last name of employee", required = true)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Schema(pattern = "^[A-Z0-9+_.-]+@[A-Z0-9.-]+$",
            description = "Email of employee",
            required = true,
            example = "employee.employeeson@gmail.com")
    private String email;
}
