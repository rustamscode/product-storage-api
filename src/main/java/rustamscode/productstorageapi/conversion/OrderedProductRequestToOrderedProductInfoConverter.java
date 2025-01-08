package rustamscode.productstorageapi.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.service.dto.OrderedProductInfo;
import rustamscode.productstorageapi.web.dto.OrderedProductRequest;

@Component
public class OrderedProductRequestToOrderedProductInfoConverter
    implements Converter<OrderedProductRequest, OrderedProductInfo> {

  @Override
  public OrderedProductInfo convert(final OrderedProductRequest request) {
    return OrderedProductInfo.builder()
        .id(request.getId())
        .amount(request.getAmount())
        .build();
  }
}
