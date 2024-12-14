package rustamscode.productstorageapi.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(UUID id) {
        super("The product with ID " + id + " was not found.");
    }
}