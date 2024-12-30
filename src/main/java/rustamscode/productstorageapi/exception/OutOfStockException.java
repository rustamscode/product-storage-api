package rustamscode.productstorageapi.exception;

import java.util.UUID;

public class OutOfStockException extends RuntimeException {
  public OutOfStockException(UUID productId) {
    super("The product with id " + productId + " is out of stock!");
  }
}
