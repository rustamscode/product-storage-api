package rustamscode.productstorageapi.exception;

import java.util.UUID;

public class OrderAccessDeniedException extends RuntimeException {
  public OrderAccessDeniedException(final UUID orderId, final Long customerId) {
    super("Order with ID " + orderId + " does not belong to customer with ID " + customerId);
  }
}
