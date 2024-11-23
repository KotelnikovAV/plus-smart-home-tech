drop table if exists products;

CREATE TABLE IF NOT EXISTS products (
    product_id          BIGINT            GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_name        VARCHAR            NOT NULL,
    description         VARCHAR            NOT NULL,
    image_src           VARCHAR            NOT NULL,
    quantity_state      VARCHAR            NOT NULL,
    product_state       VARCHAR            NOT NULL,
    rating              DOUBLE PRECISION,
    product_category    VARCHAR            NOT NULL,
    price               DOUBLE PRECISION   NOT NULL
);