package com.knubisoft.testapi.model.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiTestEntity {
    private String name;
    private String description;
    private String status;
    private String category;
    private UploadedFileInfo uploadedFile;
}