package rustamscode.productstorageapi.web.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import rustamscode.productstorageapi.enumeration.OrderStatus;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatusChangeRequest {
  OrderStatus status;
}
