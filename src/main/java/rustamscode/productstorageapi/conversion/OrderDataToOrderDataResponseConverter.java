package rustamscode.productstorageapi.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.service.dto.OrderData;
import rustamscode.productstorageapi.service.dto.OrderedProductDataObject;
import rustamscode.productstorageapi.web.dto.response.OrderDataResponse;
import rustamscode.productstorageapi.web.dto.response.OrderedProductDataResponse;

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

  private OrderedProductDataResponse convertToOrderedProductDataResponse(final OrderedProductDataObject productDataObject) {
    return OrderedProductDataResponse.builder()
        .productId(productDataObject.getProductId())
        .name(productDataObject.getName())
        .price(productDataObject.getPrice())
        .amount(productDataObject.getAmount())
        .build();
  }
}
