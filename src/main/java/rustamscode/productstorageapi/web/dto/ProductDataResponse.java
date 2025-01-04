package rustamscode.productstorageapi.web.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import rustamscode.productstorageapi.enumeration.Category;
import rustamscode.productstorageapi.enumeration.Currency;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents detailed information about a product.
 * This class serves as a data transfer object in a service layer
 * and is used for passing product data to controller layer.
 */


@Setter
@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDataResponse {
    UUID id;
    String name;
    BigInteger productNumber;
    String info;
    Category category;
    BigDecimal price;
    BigDecimal amount;
    LocalDateTime lastAmountUpdate;
    LocalDate creationTime;
    Currency currency;
}