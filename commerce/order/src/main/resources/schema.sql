drop table if exists order_cart_mapping, shopping_cart;

CREATE TABLE IF NOT EXISTS orders (
    id                  VARCHAR             PRIMARY KEY,
    shopping_cart_id    VARCHAR             NOT NULL,
    payment_id          VARCHAR             NOT NULL,
    delivery_id         VARCHAR             NOT NULL,
    state               VARCHAR             NOT NULL,
    delivery_weight     DOUBLE PRECISION    NOT NULL,
    delivery_volume     DOUBLE PRECISION    NOT NULL,
    fragile             BOOLEAN             NOT NULL,
    total_price         DOUBLE PRECISION    NOT NULL,
    delivery_price      DOUBLE PRECISION    NOT NULL,
    product_price       DOUBLE PRECISION    NOT NULL
);

CREATE TABLE IF NOT EXISTS order_cart_mapping (
    cart_id         VARCHAR     REFERENCES orders (id) ON DELETE CASCADE ON UPDATE CASCADE,
    product_id      VARCHAR     NOT NULL,
    quantity        BIGINT      NOT NULL
);