package rustamscode.productstorageapi.web.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import rustamscode.productstorageapi.persistance.entity.product.Category;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Request object for a controller layer.
 * This class contains the necessary fields and validation constraints
 * for passing product update details to service layer.
 */

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    @NotNull
    @Min(value = 0, message = "Product number must be 0 or greater!")
    BigInteger productNumber;

    @NotEmpty
    @Size(min = 1, max = 1000, message = "Info must not be empty or exceed 1000 characters!")
    String info;

    @NotNull(message = "Category must not be null!")
    Category category;

    @NotNull
    @DecimalMin(value = "0.00", message = "Price must be 0 or greater!")
    BigDecimal price;

    @NotNull
    @Min(value = 0, message = "Amount must be 0 or greater!")
    BigDecimal amount;
}