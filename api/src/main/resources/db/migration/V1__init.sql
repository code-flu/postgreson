--
-- json_b
--
CREATE TABLE IF NOT EXISTS json_b (
    id      uuid NOT NULL,
    json    JSONB NOT NULL,
    PRIMARY KEY (id)
);