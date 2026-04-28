#!/bin/bash

ZIP_FILE_NAME=lambda-functions.zip

awslocal lambda create-function \
        --function-name=http-request-python \
        --runtime python3.9 \
        --zip-file fileb:///etc/localstack/init/ready.d/$ZIP_FILE_NAME \
        --handler mega-api-python.lambda_handler \
        --role arn:aws:iam::000000000000:role/lambda-role \
        --region eu-central-1

awslocal lambda create-function \
        --function-name=hello-world-python \
        --runtime python3.9 \
        --zip-file fileb:///etc/localstack/init/ready.d/$ZIP_FILE_NAME \
        --handler hello-world-python.lambda_handler \
        --role arn:aws:iam::000000000000:role/lambda-role \
        --region eu-central-1

awslocal lambda create-function \
        --function-name=student-scores-python \
        --runtime python3.9 \
        --zip-file fileb:///etc/localstack/init/ready.d/$ZIP_FILE_NAME \
        --handler student-scores-python.lambda_handler \
        --role arn:aws:iam::000000000000:role/lambda-role \
        --region eu-central-1

awslocal lambda create-function \
        --function-name=http-request-js \
        --runtime nodejs18.x \
        --zip-file fileb:///etc/localstack/init/ready.d/$ZIP_FILE_NAME \
        --handler mega-api-https.handler \
        --role arn:aws:iam::000000000000:role/lambda-role \
        --region eu-central-1
