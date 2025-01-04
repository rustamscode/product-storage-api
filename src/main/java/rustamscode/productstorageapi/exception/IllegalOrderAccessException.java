package rustamscode.productstorageapi.exception;

import rustamscode.productstorageapi.enumeration.OrderStatus;

public class IllegalOrderAccessException extends RuntimeException {
  public IllegalOrderAccessException(final OrderStatus status) {
    super("You must not update order that is not " + OrderStatus.CREATED);
  }
}
