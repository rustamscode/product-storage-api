package rustamscode.productstorageapi.service.dto;

import lombok.Builder;
import lombok.Value;
import rustamscode.productstorageapi.enumeration.Category;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Request object for updating an existing product.
 */

@Value
@Builder
public class ImmutableProductUpdateDetails {
  BigInteger productNumber;
  String info;
  Category category;
  BigDecimal price;
  BigDecimal amount;
}