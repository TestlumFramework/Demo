package com.knubisoft.testapi.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.knubisoft.testapi.repository")
@ConditionalOnExpression("${dynamodb.enabled:true}")
public class DynamoDBConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String dynamoDBEndpoint;

    @Value("${amazon.aws.region}")
    private String awsSigningRegion;

    @Value("${amazon.aws.accesskey}")
    private String awsAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String awsSecretKey;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
                new AwsClientBuilder.EndpointConfiguration(dynamoDBEndpoint, awsSigningRegion);

        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(amazonAWSCredentialsProvider())
                .build();
    }

    public AWSCredentialsProvider amazonAWSCredentialsProvider() {
        return new AWSStaticCredentialsProvider(amazonAWSCredentials());
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(awsAccessKey, awsSecretKey);
    }
}
