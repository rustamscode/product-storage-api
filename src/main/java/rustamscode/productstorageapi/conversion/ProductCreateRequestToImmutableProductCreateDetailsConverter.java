package rustamscode.productstorageapi.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.service.dto.ImmutableProductCreateDetails;
import rustamscode.productstorageapi.web.dto.ProductCreateRequest;

import java.time.LocalDateTime;

@Component
public class ProductCreateRequestToImmutableProductCreateDetailsConverter implements
    Converter<ProductCreateRequest, ImmutableProductCreateDetails> {
  @Override
  public ImmutableProductCreateDetails convert(ProductCreateRequest productCreateRequest) {
    return ImmutableProductCreateDetails.builder()
        .name(productCreateRequest.getName())
        .productNumber(productCreateRequest.getProductNumber())
        .info(productCreateRequest.getInfo())
        .category(productCreateRequest.getCategory())
        .price(productCreateRequest.getPrice())
        .amount(productCreateRequest.getAmount())
        .lastAmountUpdate(LocalDateTime.now())
        .build();
  }
}
