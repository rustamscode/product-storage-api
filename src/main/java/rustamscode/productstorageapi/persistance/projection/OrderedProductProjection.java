package rustamscode.productstorageapi.persistance.projection;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
public class OrderedProductProjection {
  UUID productId;
  String name;
  BigDecimal amount;
  BigDecimal price;
}
