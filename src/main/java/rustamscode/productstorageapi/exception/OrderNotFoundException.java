package rustamscode.productstorageapi.exception;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException(final UUID orderId) {
    super("Order with id " + orderId + " was not found");
  }
}
