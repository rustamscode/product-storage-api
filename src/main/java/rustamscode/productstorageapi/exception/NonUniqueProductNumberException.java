package rustamscode.productstorageapi.exception;

import java.math.BigInteger;
import java.util.UUID;

public class NonUniqueProductNumberException extends RuntimeException {
  public NonUniqueProductNumberException(BigInteger productNumber) {
    super("The product with product number " + productNumber + " already exists.");
  }

  public NonUniqueProductNumberException(BigInteger productNumber, UUID id) {
    super("The product with product number " + productNumber
          + " already exists with ID " + id + ".");
  }
}
