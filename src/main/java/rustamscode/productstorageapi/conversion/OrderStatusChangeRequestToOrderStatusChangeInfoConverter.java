package rustamscode.productstorageapi.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.service.dto.OrderStatusChangeInfo;
import rustamscode.productstorageapi.web.dto.OrderStatusChangeRequest;

@Component
public class OrderStatusChangeRequestToOrderStatusChangeInfoConverter
    implements Converter<OrderStatusChangeRequest, OrderStatusChangeInfo> {

  @Override
  public OrderStatusChangeInfo convert(OrderStatusChangeRequest request) {
    return OrderStatusChangeInfo.builder()
        .status(request.getStatus())
        .build();
  }
}
