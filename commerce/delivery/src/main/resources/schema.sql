drop table if exists addresses, delivery;

CREATE TABLE IF NOT EXISTS addresses (
    id          BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    country     VARCHAR     NOT NULL,
    city        VARCHAR     NOT NULL,
    street      VARCHAR     NOT NULL,
    house       VARCHAR     NOT NULL,
    flat        VARCHAR     NOT NULL
);

CREATE TABLE IF NOT EXISTS delivery (
    delivery_id         VARCHAR     PRIMARY KEY,
    from_address_id     BIGINT      REFERENCES addresses (id) ON DELETE CASCADE ON UPDATE CASCADE,
    to_address_id       BIGINT      REFERENCES addresses (id) ON DELETE CASCADE ON UPDATE CASCADE,
    order_id            VARCHAR     NOT NULL,
    delivery_state      VARCHAR     NOT NULL
);