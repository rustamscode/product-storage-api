package rustamscode.productstorageapi.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.persistance.projection.OrderedProductProjection;
import rustamscode.productstorageapi.service.dto.OrderData;
import rustamscode.productstorageapi.web.dto.OrderDataResponse;
import rustamscode.productstorageapi.web.dto.OrderedProductDataResponse;

import java.util.stream.Collectors;

@Component
public class OrderDataToOrderDataResponseConverter implements Converter<OrderData, OrderDataResponse> {
  @Override
  public OrderDataResponse convert(final OrderData orderData) {
    return OrderDataResponse.builder()
        .orderId(orderData.getOrderId())
        .orderedProducts(orderData.getOrderedProducts().stream()
            .map(this::convertToOrderedProductDataResponse)
            .collect(Collectors.toList())
        )
        .build();
  }

  private OrderedProductDataResponse convertToOrderedProductDataResponse(final OrderedProductProjection orderedProductProjection) {
    return OrderedProductDataResponse.builder()
        .productId(orderedProductProjection.getProductId())
        .name(orderedProductProjection.getName())
        .price(orderedProductProjection.getPrice())
        .amount(orderedProductProjection.getAmount())
        .build();
  }
}
