CREATE TABLE ordered_products
(   id UUID PRIMARY KEY,
    order_id               UUID REFERENCES orders (id),
    ordered_product_amount NUMERIC(10, 2)               NOT NULL,
    ordered_product_price  NUMERIC(10, 2)               NOT NULL,
    product_id             UUID REFERENCES products (id) NOT NULL
);