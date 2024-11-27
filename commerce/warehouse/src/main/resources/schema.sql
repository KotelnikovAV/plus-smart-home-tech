drop table if exists dimension, warehouse;

CREATE TABLE IF NOT EXISTS dimension (
    id              BIGINT              GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    width           DOUBLE PRECISION    NOT NULL,
    height          DOUBLE PRECISION    NOT NULL,
    depth           DOUBLE PRECISION    NOT NULL
);

CREATE TABLE IF NOT EXISTS warehouse (
    product_id      VARCHAR             PRIMARY KEY,
    fragile         BOOLEAN             NOT NULL,
    dimension_id    BIGINT              REFERENCES dimension (id) ON DELETE CASCADE ON UPDATE CASCADE,
    weight          DOUBLE PRECISION    NOT NULL,
    quantity        BIGINT              NOT NULL
);