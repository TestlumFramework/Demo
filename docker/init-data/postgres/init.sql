CREATE SEQUENCE IF NOT EXISTS news_id_seq START 1;

CREATE TABLE IF NOT EXISTS news
(
    id         BIGINT       NOT NULL DEFAULT nextval('news_id_seq') PRIMARY KEY,
    newsName   VARCHAR(512) NOT NULL UNIQUE,
    newsNumber BIGINT       NOT NULL,
    active     BOOLEAN      NOT NULL DEFAULT false,
    createdAt  TIMESTAMP    NOT NULL
);
