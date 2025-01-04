package rustamscode.productstorageapi.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.persistance.entity.ProductEntity;
import rustamscode.productstorageapi.service.dto.ImmutableProductCreateDetails;

@Component
public class ImmutableProductCreateDetailsToProductConverter implements
    Converter<ImmutableProductCreateDetails, ProductEntity> {

  @Override
  public ProductEntity convert(ImmutableProductCreateDetails immutableProductCreateRequest) {
    return ProductEntity.builder()
        .name(immutableProductCreateRequest.getName())
        .productNumber(immutableProductCreateRequest.getProductNumber())
        .info(immutableProductCreateRequest.getInfo())
        .category(immutableProductCreateRequest.getCategory())
        .price(immutableProductCreateRequest.getPrice())
        .amount(immutableProductCreateRequest.getAmount())
        .lastAmountUpdate(immutableProductCreateRequest.getLastAmountUpdate())
        .build();
  }
}