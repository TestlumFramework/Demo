CREATE TABLE IF NOT EXISTS news (
    id         BIGINT auto_increment not null primary key,
    newsName   varchar(512)          not null unique,
    newsNumber BIGINT                not null,
    active     boolean default false not null,
    createdAt  timestamp             not null
);