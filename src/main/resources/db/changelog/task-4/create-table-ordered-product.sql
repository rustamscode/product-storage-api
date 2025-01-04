CREATE TABLE ordered_product
(
    order_id               UUID REFERENCES "order" (id),
    amount NUMERIC(10, 2)               NOT NULL,
    price  NUMERIC(10, 2)               NOT NULL,
    product_id             UUID REFERENCES product (id) NOT NULL,

    CONSTRAINT order_product_pk PRIMARY KEY (order_id, product_id)
);