package com.knubisoft.testapi.model.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadedFileInfo {
    private String uploadedFileName;
    private Long uploadedFileSize;
    private String uploadedFileContentType;
}