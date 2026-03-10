#!/usr/bin/env sh

CLICKHOUSE_DB="${CLICKHOUSE_DB:-database}";
CLICKHOUSE_USER="${CLICKHOUSE_USER:-user}";
CLICKHOUSE_PASSWORD="${CLICKHOUSE_PASSWORD:-password}";
INIT_FLAG="/init.flag"

if [ ! -f "$INIT_FLAG" ]; then

cat <<EOT >> /etc/clickhouse-server/users.d/user.xml
<yandex>
  <!-- Docs: <https://clickhouse.tech/docs/en/operations/settings/settings_users/> -->
  <users>
    <${CLICKHOUSE_USER}>
      <profile>default</profile>
      <networks>
        <ip>::/0</ip>
      </networks>
      <password>${CLICKHOUSE_PASSWORD}</password>
      <quota>default</quota>
    </${CLICKHOUSE_USER}>
  </users>
</yandex>
EOT
cat /etc/clickhouse-server/users.d/user.xml;

clickhouse-client --user "${CLICKHOUSE_USER}" --password "${CLICKHOUSE_PASSWORD}" --query "CREATE DATABASE IF NOT EXISTS ${CLICKHOUSE_DB};"
clickhouse-client --user "${CLICKHOUSE_USER}" --password "${CLICKHOUSE_PASSWORD}" --database "${CLICKHOUSE_DB}" --query "CREATE TABLE IF NOT EXISTS news
(
id            Int256,
newsName      String,
newsNumber    Int256,
active        UInt8      DEFAULT 0,
createdAt     DateTime64
) ENGINE = MergeTree()
ORDER BY (id);"

touch "$INIT_FLAG"
fi
