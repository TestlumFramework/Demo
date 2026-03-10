package com.knubisoft.testapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Validated
public class NewsDto {

    @NotBlank(message = "Name is required")
    @Schema(description = "News name", required = true)
    private String name;

    @NotNull(message = "Number is required")
    @Min(value = 0)
    @Schema(description = "News number", required = true)
    private Integer number;

    @Schema(description = "Is news active")
    private boolean active;

    @NotNull(message = "Created dateTime is required")
    @Schema(description = "News created dateTime", pattern = "yyyy-MM-ddTHH:mm:ss", example = "2020-02-02T20:00:02")
    private LocalDateTime createdAt;
}
