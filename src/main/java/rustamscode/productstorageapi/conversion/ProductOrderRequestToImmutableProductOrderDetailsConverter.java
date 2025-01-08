package rustamscode.productstorageapi.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.service.dto.ImmutableProductOrderDetails;
import rustamscode.productstorageapi.service.dto.OrderedProductInfo;
import rustamscode.productstorageapi.web.dto.CreateOrderRequest;
import rustamscode.productstorageapi.web.dto.OrderedProductRequest;

import java.util.stream.Collectors;

@Component
public class ProductOrderRequestToImmutableProductOrderDetailsConverter
    implements Converter<CreateOrderRequest, ImmutableProductOrderDetails> {

  @Override
  public ImmutableProductOrderDetails convert(CreateOrderRequest request) {
    return ImmutableProductOrderDetails.builder()
        .deliveryAddress(request.getDeliveryAddress())
        .products(request.getProducts().stream()
            .map(r -> convertToOrderedProductObject(r))
            .collect(Collectors.toList()))
        .build();
  }

  private OrderedProductInfo convertToOrderedProductObject(OrderedProductRequest request) {
    return OrderedProductInfo.builder()
        .id(request.getId())
        .amount(request.getAmount())
        .build();
  }
}
