CREATE SEQUENCE IF NOT EXISTS users_id_seq START 3;

CREATE TABLE IF NOT EXISTS users (
                       id BIGINT NOT NULL DEFAULT nextval('users_id_seq') PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(255) NOT NULL,
                       is_account_non_expired BOOLEAN NOT NULL,
                       is_account_non_locked BOOLEAN NOT NULL,
                       is_credentials_non_expired BOOLEAN NOT NULL,
                       is_enabled BOOLEAN NOT NULL
);