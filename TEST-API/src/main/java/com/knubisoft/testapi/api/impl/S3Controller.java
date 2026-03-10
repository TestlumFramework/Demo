package com.knubisoft.testapi.api.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.knubisoft.testapi.api.S3Api;
import com.knubisoft.testapi.dto.FileDetailsDto;
import com.knubisoft.testapi.exception.ResourceAlreadyExistsException;
import com.knubisoft.testapi.exception.ResourceNotFoundException;
import com.knubisoft.testapi.util.Message;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.knubisoft.testapi.util.S3FileUtils.getFileMetadata;
import static com.knubisoft.testapi.util.S3FileUtils.getHttpHeaders;
import static java.lang.String.format;

@Slf4j
@RestController
@RequiredArgsConstructor
@ConditionalOnExpression("${s3.enabled:true}")
public class S3Controller implements S3Api {

    private final AmazonS3 s3Client;
    @Value("${aws.s3.region}")
    private String region;

    public ResponseEntity<String> createBucket(final String bucketName) {
        if (s3Client.doesBucketExistV2(bucketName)) {
            throw new ResourceAlreadyExistsException(Bucket.class, bucketName);
        }
        s3Client.createBucket(new CreateBucketRequest(bucketName, region));
        return ResponseEntity.ok(format(Message.BUCKET_CREATED, bucketName));
    }

    public ResponseEntity<String> deleteBucket(final String bucketName) {
        checkBucketExistence(bucketName);
        s3Client.deleteBucket(bucketName);
        return ResponseEntity.ok(format(Message.BUCKET_REMOVED, bucketName));
    }

    public ResponseEntity<List<String>> listAllBuckets() {
        List<String> bucketsNames = s3Client.listBuckets().stream()
                .map(Bucket::getName)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bucketsNames);
    }

    @SneakyThrows
    public ResponseEntity<String> upload(final String bucketName, final MultipartFile file) {
        checkBucketExistence(bucketName);
        String fileName = file.getOriginalFilename();
        s3Client.putObject(bucketName, fileName, file.getInputStream(), getFileMetadata(file));
        return ResponseEntity.ok(format(Message.FILE_UPLOADED, fileName, bucketName));
    }

    public ResponseEntity<FileDetailsDto> outputDetails(final String bucketName, final String fileName) {
        checkBucketExistence(bucketName);
        checkBucketFile(fileName, bucketName);
        S3Object file = s3Client.getObject(bucketName, fileName);
        ObjectMetadata metadata = file.getObjectMetadata();
        FileDetailsDto dto = new FileDetailsDto(fileName, metadata.getContentLength(), metadata.getContentType());
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<InputStreamResource> download(final String bucketName, final String fileName) {
        checkBucketExistence(bucketName);
        checkBucketFile(fileName, bucketName);
        S3Object object = s3Client.getObject(bucketName, fileName);
        return ResponseEntity.ok()
                .headers(getHttpHeaders(fileName, object.getObjectMetadata().getContentLength()))
                .body(new InputStreamResource(object.getObjectContent()));
    }

    public ResponseEntity<String> remove(final String bucketName, final String fileName) {
        checkBucketExistence(bucketName);
        checkBucketFile(fileName, bucketName);
        s3Client.deleteObject(bucketName, fileName);
        return ResponseEntity.ok(format(Message.FILE_REMOVED, fileName, bucketName));
    }

    public ResponseEntity<List<String>> listAll(final String bucketName) {
        checkBucketExistence(bucketName);
        List<String> filesNames = s3Client.listObjectsV2(bucketName)
                .getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
        return ResponseEntity.ok(filesNames);
    }

    public ResponseEntity<String> removeAll(final String bucketName) {
        checkBucketExistence(bucketName);
        ListObjectsV2Result listOfFiles = s3Client.listObjectsV2(bucketName);
        listOfFiles.getObjectSummaries().forEach(object -> s3Client.deleteObject(bucketName, object.getKey()));
        return ResponseEntity.ok(format(Message.FILES_REMOVED, bucketName));
    }

    private void checkBucketExistence(final String bucketName) {
        if (!s3Client.doesBucketExistV2(bucketName)) {
            throw new ResourceNotFoundException(Bucket.class, bucketName);
        }
    }

    private void checkBucketFile(String fileName, String bucketName) {
        if (!s3Client.doesObjectExist(bucketName, fileName)) {
            throw new ResourceNotFoundException(MultipartFile.class, fileName);
        }
    }
}
