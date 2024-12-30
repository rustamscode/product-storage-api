CREATE TABLE ordered_product
(   id UUID PRIMARY KEY,
    order_id               UUID REFERENCES "order" (id),
    ordered_product_amount NUMERIC(10, 2)               NOT NULL,
    ordered_product_price  NUMERIC(10, 2)               NOT NULL,
    product_id             UUID REFERENCES product (id) NOT NULL
);