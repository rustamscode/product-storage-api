package rustamscode.productstorageapi.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.service.dto.ImmutableOrderedProductObject;
import rustamscode.productstorageapi.web.dto.request.OrderedProductRequest;

@Component
public class OrderedProductRequestToImmutableOrderedProductObjectConverter
    implements Converter<OrderedProductRequest, ImmutableOrderedProductObject> {

  @Override
  public ImmutableOrderedProductObject convert(OrderedProductRequest request) {
    return ImmutableOrderedProductObject.builder()
        .id(request.getId())
        .amount(request.getAmount())
        .build();
  }
}
