package rustamscode.productstorageapi.conversion;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.service.dto.ImmutableOrderedProductObject;
import rustamscode.productstorageapi.service.dto.ImmutableProductOrderDetails;
import rustamscode.productstorageapi.web.dto.request.ProductOrderRequest;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductOrderRequestToImmutableProductOrderDetailsConverter
    implements Converter<ProductOrderRequest, ImmutableProductOrderDetails> {
  final ConversionService conversionService;

  @Override
  public ImmutableProductOrderDetails convert(ProductOrderRequest request) {
    return ImmutableProductOrderDetails.builder()
        .deliveryAddress(request.getDeliveryAddress())
        .products(request.getProducts().stream()
            .map(r -> conversionService.convert(r, ImmutableOrderedProductObject.class))
            .collect(Collectors.toList()))
        .build();
  }
}
