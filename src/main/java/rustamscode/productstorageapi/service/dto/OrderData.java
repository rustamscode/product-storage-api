package rustamscode.productstorageapi.service.dto;

import lombok.Builder;
import lombok.Value;
import rustamscode.productstorageapi.persistance.projection.OrderedProductProjection;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class OrderData {
  UUID orderId;
  List<OrderedProductProjection> orderedProducts;
  BigDecimal totalPrice;
}
