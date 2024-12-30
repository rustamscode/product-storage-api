package rustamscode.productstorageapi.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.service.dto.ImmutableProductUpdateDetails;
import rustamscode.productstorageapi.web.dto.request.ProductUpdateRequest;

@Component
public class ProductUpdateRequestToImmutableProductUpdateDetailsConverter implements
    Converter<ProductUpdateRequest, ImmutableProductUpdateDetails> {
  @Override
  public ImmutableProductUpdateDetails convert(ProductUpdateRequest productUpdateRequest) {
    return ImmutableProductUpdateDetails.builder()
        .productNumber(productUpdateRequest.getProductNumber())
        .info(productUpdateRequest.getInfo())
        .category(productUpdateRequest.getCategory())
        .price(productUpdateRequest.getPrice())
        .amount(productUpdateRequest.getAmount())
        .build();
  }
}