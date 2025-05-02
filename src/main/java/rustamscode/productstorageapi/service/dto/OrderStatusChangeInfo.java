package rustamscode.productstorageapi.service.dto;

import lombok.Builder;
import lombok.Value;
import rustamscode.productstorageapi.enumeration.OrderStatus;

@Value
@Builder
public class OrderStatusChangeInfo {
  OrderStatus status;
}
