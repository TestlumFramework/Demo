package com.knubisoft.testapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
@ConditionalOnExpression("${amazon.sqs.enabled:true}")
public class SqsConfiguration {

    @Value("${amazon.sqs.queue.name}")
    private String queueName;

    @Value("${amazon.sqs.access-key}")
    private String accessKey;

    @Value("${amazon.sqs.secret-key}")
    private String secretKey;

    @Value("${amazon.sqs.service-endpoint}")
    private String serviceEndpoint;

    @Value("${amazon.sqs.region}")
    private String region;

    @Bean
    public SqsClient amazonSQSClient() {
        AwsBasicCredentials basicAWSCredentials =
                AwsBasicCredentials.create(accessKey, secretKey);
        StaticCredentialsProvider awsStaticCredentialsProvider = StaticCredentialsProvider.create(basicAWSCredentials);
        SqsClient sqsClient = SqsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(awsStaticCredentialsProvider)
                .endpointOverride(URI.create(serviceEndpoint))
                .build();

        sqsClient.createQueue(request -> request.queueName(queueName));
        return sqsClient;
    }
}
