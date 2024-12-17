package rustamscode.productstorageapi.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.math.BigInteger;

@Value
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImmutableProductFilterDetails {
    String name;
    BigDecimal price;
    BigInteger amount;
    Integer page;
    Integer size;
}
