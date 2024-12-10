drop table if exists shopping_cart_mapping, shopping_cart;

CREATE TABLE IF NOT EXISTS shopping_cart (
    id          VARCHAR      PRIMARY KEY,
    username    VARCHAR      NOT NULL,
    active      BOOLEAN      NOT NULL
);

CREATE TABLE IF NOT EXISTS shopping_cart_mapping (
    cart_id         VARCHAR     REFERENCES shopping_cart (id) ON DELETE CASCADE ON UPDATE CASCADE,
    product_id      VARCHAR     NOT NULL,
    quantity        BIGINT      NOT NULL
);