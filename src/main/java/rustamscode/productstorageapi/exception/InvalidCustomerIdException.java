package rustamscode.productstorageapi.exception;

public class InvalidCustomerIdException extends RuntimeException {
  public InvalidCustomerIdException(String customerIdHeader) {
    super(customerIdHeader);
  }
}
