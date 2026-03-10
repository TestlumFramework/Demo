package com.knubisoft.testapi.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class S3Config {
    @Value("${aws.s3.access.key}")
    private String accessKeyId;
    @Value("${aws.s3.secret.key}")
    private String secretKey;
    @Value("${aws.s3.endpoint}")
    private String endpoint;
    @Value("${aws.s3.region}")
    private String region;

    @Bean
    public AmazonS3 initS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretKey);
        AwsClientBuilder.EndpointConfiguration endpointConfig =
                new AwsClientBuilder.EndpointConfiguration(endpoint, region);
        AWSStaticCredentialsProvider credentials =
                new AWSStaticCredentialsProvider(awsCredentials);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(credentials)
                .withEndpointConfiguration(endpointConfig)
                .withPathStyleAccessEnabled(true)
                .build();
    }
}