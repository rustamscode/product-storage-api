package rustamscode.productstorageapi.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.persistance.entity.ProductEntity;
import rustamscode.productstorageapi.service.dto.ProductData;


@Component
public class ProductEntityToProductDataConverter implements Converter<ProductEntity, ProductData> {

  @Override
  public ProductData convert(ProductEntity product) {
    return ProductData.builder()
        .id(product.getId())
        .name(product.getName())
        .productNumber(product.getProductNumber())
        .info(product.getInfo())
        .category(product.getCategory())
        .price(product.getPrice())
        .amount(product.getAmount())
        .lastAmountUpdate(product.getLastAmountUpdate())
        .creationTime(product.getCreationTime().toLocalDate())
        .build();
  }
}