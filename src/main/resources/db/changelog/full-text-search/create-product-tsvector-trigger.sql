CREATE TRIGGER tsvectorupdate
    BEFORE INSERT OR UPDATE
    ON public.product
    FOR EACH ROW
EXECUTE FUNCTION product_tsvector_trigger();