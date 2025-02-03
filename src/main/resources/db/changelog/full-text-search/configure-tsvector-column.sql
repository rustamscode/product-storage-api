UPDATE product
SET tsvector_column = to_tsvector('english', info);
