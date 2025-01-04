package rustamscode.productstorageapi.service.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class OrderData {
  UUID orderId;
  List<OrderedProductDataObject> orderedProducts;
}
