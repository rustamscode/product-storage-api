package rustamscode.productstorageapi.web.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
public class OrderedProductDataResponse {
  UUID productId;
  String name;
  BigDecimal amount;
  BigDecimal price;
}
