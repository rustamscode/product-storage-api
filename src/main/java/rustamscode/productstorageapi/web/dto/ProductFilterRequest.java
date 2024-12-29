package rustamscode.productstorageapi.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.math.BigInteger;

@Value
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFilterRequest {
    String name;
    BigDecimal price;
    BigInteger amount;

    @NotNull
    Integer page;

    @NotNull
    Integer size;
}
