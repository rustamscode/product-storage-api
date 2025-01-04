package rustamscode.productstorageapi.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.service.dto.ImmutableOrderedProductObject;
import rustamscode.productstorageapi.service.dto.ImmutableProductOrderDetails;
import rustamscode.productstorageapi.web.dto.request.OrderedProductRequest;
import rustamscode.productstorageapi.web.dto.request.ProductOrderRequest;

import java.util.stream.Collectors;

@Component
public class ProductOrderRequestToImmutableProductOrderDetailsConverter
    implements Converter<ProductOrderRequest, ImmutableProductOrderDetails> {

  @Override
  public ImmutableProductOrderDetails convert(ProductOrderRequest request) {
    return ImmutableProductOrderDetails.builder()
        .deliveryAddress(request.getDeliveryAddress())
        .products(request.getProducts().stream()
            .map(r -> convertToOrderedProductObject(r))
            .collect(Collectors.toList()))
        .build();
  }

  private ImmutableOrderedProductObject convertToOrderedProductObject(OrderedProductRequest request) {
    return ImmutableOrderedProductObject.builder()
        .id(request.getId())
        .amount(request.getAmount())
        .build();
  }
}
