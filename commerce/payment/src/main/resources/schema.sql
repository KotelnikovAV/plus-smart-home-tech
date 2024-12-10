drop table if exists payments;

CREATE TABLE IF NOT EXISTS payments (
    payment_id      VARCHAR             PRIMARY KEY,
    total_payment   DOUBLE PRECISION    NOT NULL,
    delivery_total  DOUBLE PRECISION    NOT NULL,
    fee_total       DOUBLE PRECISION    NOT NULL,
    payment_status  VARCHAR             NOT NULL
);