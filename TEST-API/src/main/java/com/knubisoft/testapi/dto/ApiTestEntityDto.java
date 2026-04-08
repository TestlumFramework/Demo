package com.knubisoft.testapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ApiTestEntityDto {

    @NotBlank(message = "Name must not be blank")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9 _-]+$",
            message = "Name can contain only letters, numbers, spaces, underscores and hyphens"
    )
    private String name;

    @NotBlank(message = "Description must not be blank")
    @Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    private String description;

    @NotBlank(message = "Status must not be blank")
    @Pattern(
            regexp = "^(NEW|ACTIVE|INACTIVE|ARCHIVED)$",
            message = "Status must be one of: NEW, ACTIVE, INACTIVE, ARCHIVED"
    )
    private String status;

    @NotBlank(message = "Category must not be blank")
    @Pattern(
            regexp = "^[A-Z_]+$",
            message = "Category must contain only uppercase letters and underscores"
    )
    @Size(min = 3, max = 30, message = "Category must be between 3 and 30 characters")
    private String category;
}