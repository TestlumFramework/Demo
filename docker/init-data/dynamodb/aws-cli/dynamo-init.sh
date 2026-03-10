#!/usr/bin/env bash

if [[ -z "$AWS_ACCESS_KEY_ID" || -z "$AWS_SECRET_ACCESS_KEY" ]]; then
  echo "AWS credentials not set!"
  exit 1
fi

echo "access key: $AWS_ACCESS_KEY_ID"
echo "secret key: $AWS_SECRET_ACCESS_KEY"

TABLE_NAME="dynamodb_news"
ENDPOINT_URL="http://dynamodb-e2e:8000"
REGION="eu-central-1"

function drop_db() {
  check_db
  if [ $? -eq 0 ]; then
    echo "Trying to drop table ${TABLE_NAME}..."
    aws dynamodb --endpoint-url ${ENDPOINT_URL} --region ${REGION} \
      delete-table --table-name ${TABLE_NAME} || echo "Failed to drop table: $?"
    echo "Dropped table ${TABLE_NAME}"
  fi
}

function check_db() {
  echo "Start check_db..."
  aws dynamodb --endpoint-url ${ENDPOINT_URL} --region ${REGION} \
    describe-table --table-name ${TABLE_NAME} >/dev/null 2>&1
  echo "Finish check_db..."
}

function create_db() {
  echo "Trying to create table ${TABLE_NAME}..."
  aws dynamodb --endpoint-url ${ENDPOINT_URL} --region ${REGION} \
    create-table --cli-input-json file:///init/create-table.json || echo "Failed to create table: $?"
  echo "Created table ${TABLE_NAME}"
}

drop_db
create_db
