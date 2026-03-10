CONNECT system/oracle@XE;

CREATE USER c##testlum IDENTIFIED BY password;

GRANT ALL PRIVILEGES TO c##testlum;

CONNECT c##testlum/password@XE;

CREATE SEQUENCE news_id_seq START WITH 1;

CREATE TABLE NEWS
(
    id         INTEGER   DEFAULT ON NULL news_id_seq.nextval PRIMARY KEY,
    newsName   VARCHAR(512)        NOT NULL UNIQUE,
    newsNumber INTEGER             NOT NULL UNIQUE,
    active     NUMBER(1) DEFAULT 0 NOT NULL,
    createdAt  TIMESTAMP           NOT NULL
);
