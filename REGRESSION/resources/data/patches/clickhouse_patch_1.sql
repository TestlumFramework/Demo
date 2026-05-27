CREATE TABLE IF NOT EXISTS news
(
    id            Int256,
    newsName      String,
    newsNumber    Int256,
    active        UInt8      DEFAULT 0,
    createdAt     DateTime64
) ENGINE = MergeTree()
ORDER BY (id);

INSERT INTO news (id, newsName, newsNumber, active, createdAt)
VALUES (21, 'Uganda', 225, 1, '2021-01-01 14:00:00');