CREATE TABLE customers
(
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(32) UNIQUE NOT NULL,
    email VARCHAR(64) NOT NULL,
    is_active BOOLEAN NOT NULL
);

COMMENT ON TABLE customers IS 'Customers of the shop';
COMMENT ON COLUMN customers.id IS 'Customer ID, serves as a primary key,'
    'can not be null and is generated automatically by AUTO_INCREMENT';
COMMENT ON COLUMN customers.login IS 'Customer''s unique login, can not be null';
COMMENT ON COLUMN customers.email IS 'Customer''s email, can not be null';
COMMENT ON COLUMN customers.is_active IS 'Current customer''s activity status '
