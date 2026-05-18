#!/bin/bash

url="http://mega-test-api:8080/api/v1/mysql/news"
json_data='{
  "name": "Shell",
  "number": 10,
  "active": true,
  "createdAt": "2023-08-14T10:50:41"
}'

# Send the HTTP POST request
response=$(curl -X POST -H "Content-Type: application/json" -d "$json_data" "$url")
