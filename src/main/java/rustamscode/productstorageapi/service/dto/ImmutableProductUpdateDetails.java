package rustamscode.productstorageapi.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import rustamscode.productstorageapi.persistance.entity.product.Category;


import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Request object for updating an existing product.
 */

@Value
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImmutableProductUpdateDetails {
    BigInteger productNumber;
    String info;
    Category category;
    BigDecimal price;
    BigDecimal amount;
}