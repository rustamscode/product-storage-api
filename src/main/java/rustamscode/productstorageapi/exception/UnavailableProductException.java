package rustamscode.productstorageapi.exception;

import java.util.UUID;

public class UnavailableProductException extends RuntimeException {
  public UnavailableProductException(UUID productId) {
    super("The product with id " + productId + " is not available");
  }
}
