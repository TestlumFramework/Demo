package com.knubisoft.testapi.util;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;


public class S3FileUtils {

    public static ObjectMetadata getFileMetadata(final MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

    public static HttpHeaders getHttpHeaders(final String fileName, long contentLength) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(contentLength);
        headers.setContentDispositionFormData("attachment", fileName);
        return headers;
    }
}
