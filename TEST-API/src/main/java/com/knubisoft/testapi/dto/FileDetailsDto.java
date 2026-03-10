package com.knubisoft.testapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileDetailsDto {
    @Schema(description = "File name", required = true)
    private String name;

    @Schema(description = "File size", required = true)
    private Long size;

    @Schema(description = "File type", required = true)
    private String type;
}
