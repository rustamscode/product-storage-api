package rustamscode.productstorageapi.service.dto;

import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Value;
import rustamscode.productstorageapi.persistance.enumeration.Category;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Request object for creating a new product.
 * This class contains all necessary fields required to create a product.
 */

@Value
@Builder
public class ImmutableProductCreateDetails {
    String name;
    BigInteger productNumber;
    String info;
    Category category;
    BigDecimal price;
    BigDecimal amount;

    @PastOrPresent
    LocalDateTime lastUpdateTime;
}