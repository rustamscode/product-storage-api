CREATE TABLE product
(
    id                 UUID         NOT NULL PRIMARY KEY,
    name               VARCHAR(255) NOT NULL,
    product_number     NUMERIC      NOT NULL UNIQUE,
    info               VARCHAR(1000),
    category           VARCHAR(50),
    price              NUMERIC(10, 2),
    amount             NUMERIC(10, 2),
    last_amount_update TIMESTAMP,
    creation_time      TIMESTAMP,
    version            BIGINT,
    is_available       BOOLEAN
);

COMMENT ON TABLE product IS 'Products in the storage';
COMMENT ON COLUMN product.id IS 'ID of the product, serves as a primary key,'
    ' can not be null and is generated automatically';
COMMENT ON COLUMN product.name IS 'Name of the product, can not be null';
COMMENT ON COLUMN product.product_number IS 'Number of the product, is unique and can not be null';
COMMENT ON COLUMN product.info IS 'Product description, can not exceed 1000 characters';
COMMENT ON COLUMN product.category IS 'Product category, is passed in as string value of an enum';
COMMENT ON COLUMN product.price IS 'Product price, a number with 10 in precision and 2 in scale';
COMMENT ON COLUMN product.amount IS 'Product amount, a number with 10 in precision and 2 in scale';
COMMENT ON COLUMN product.last_amount_update IS 'A TIMESTAMP of the latest time the product was updated';
COMMENT ON COLUMN product.creation_time IS 'A TIMESTAMP of product creation time, is generated automatically';
COMMENT ON COLUMN product.version IS 'Contains a version that is used for synchronizing';