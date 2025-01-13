package rustamscode.productstorageapi.exception;

import java.util.UUID;

public class InsufficientProductException extends RuntimeException {
  public InsufficientProductException(UUID productId) {
    super("The product with id " + productId + " is insufficient for your order!");
  }
}
