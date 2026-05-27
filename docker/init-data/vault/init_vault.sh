#!/bin/bash

# Init vault and raft autopilot config
export VAULT_ADDR="http://vault-e2e:8200"
if $(vault status | grep Initialized | awk '{print $2}'); then
    export VAULT_TOKEN="myroot"

    while $(vault status | grep Sealed | awk '{print $2}'); do sleep 5; done
    vault kv put secret/lambda/accessKey key=JQttVjEfR3y4UJ
    vault kv put secret/lambda/secretKey key=wzsTjXDJL2C5Wq
    vault kv put secret/lambda/region key=eu-central-1
    vault kv put secret/dynamo/accessKeyId key=dHwXPbnU9FVb3o
    vault kv put secret/dynamo/secretKeyId key=Kbg7XdQ2YD5hAh
    vault kv put secret/dynamo/defaultRegion key=eu-central-1
    vault kv put secret/mongo/database key=mongo_db
    vault kv put secret/mongo/graphqlDb key=graphql_db
    vault kv put secret/mongo/username key=username
    vault kv put secret/mongo/password key=password
    vault kv put secret/postgres/username key=playground
    vault kv put secret/postgres/password key=playground
    vault kv put secret/postgresAuth/username key=user
    vault kv put secret/postgresAuth/password key=password
    vault kv put secret/mysql/user username=playground
    vault kv put secret/mysql/password password=playground
    vault kv put secret/clickhouse/username key=testlum
    vault kv put secret/clickhouse/password key=testlum-clickhouse
    vault kv put secret/oracle/username key=testlum
    vault kv put secret/oracle/password key=password
    vault kv put secret/rabbit/username key=guest
    vault kv put secret/rabbit/password key=guest
    vault kv put secret/s3/accessKeyId key=DEFAULT_ACCESS_KEY
    vault kv put secret/ses/accessKeyId key=DEFAULT_ACCESS_KEY
    vault kv put secret/browserStack/user username=bohdanshakhov_fMHusx
    vault kv put secret/browserStack/accessKey key=xzf94rKxaDBUxULLwJXC
fi