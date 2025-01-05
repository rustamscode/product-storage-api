package rustamscode.productstorageapi.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.service.dto.ProductData;
import rustamscode.productstorageapi.web.dto.ProductDataResponse;

@Component
public class ProductDataToProductDataResponseConverter implements Converter<ProductData, ProductDataResponse> {

  @Override
  public ProductDataResponse convert(ProductData productData) {
    return ProductDataResponse.builder()
        .id(productData.getId())
        .name(productData.getName())
        .productNumber(productData.getProductNumber())
        .info(productData.getInfo())
        .category(productData.getCategory())
        .price(productData.getPrice())
        .amount(productData.getAmount())
        .lastAmountUpdate(productData.getLastAmountUpdate())
        .creationTime(productData.getCreationTime())
        .build();
  }
}