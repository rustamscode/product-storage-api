CREATE TABLE orders
(
    id UUID PRIMARY KEY,
    customer_id BIGINT REFERENCES customers(id) NOT NULL,
    status VARCHAR(32) NOT NULL,
    delivery_address VARCHAR(128) NOT NULL
);

COMMENT ON TABLE orders IS 'Product orders';
COMMENT ON COLUMN orders.id IS 'ID of the product, serves as a primary key,'
    ' can not be null and is generated automatically';
COMMENT ON COLUMN orders.customer_id IS 'ID of the order-owner, serves as a foreign key'
    'to customer table, can not be null';
COMMENT ON COLUMN orders.status IS 'Status of the order, possible:'
    'CREATED, CONFIRMED, CANCELLED, DONE, REJECTED, can not be null';
COMMENT ON COLUMN orders.delivery_address IS 'Order address, can not be null';