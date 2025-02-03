CREATE OR REPLACE FUNCTION product_tsvector_trigger() RETURNS trigger AS
$$
begin
    new.tsvector_column := to_tsvector('english', new.info);
    return new;
end
$$ LANGUAGE plpgsql;