package rustamscode.productstorageapi.web.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDataResponse {
  UUID orderId;
  List<OrderedProductDataResponse> orderedProducts;
  BigDecimal totalPrice;
}
