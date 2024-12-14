package rustamscode.productstorageapi.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.service.dto.ImmutableProductCreateDetails;
import rustamscode.productstorageapi.persistance.entity.product.ProductEntity;

@Component
public class ImmutableProductCreateDetailsToProductConverter implements
        Converter<ImmutableProductCreateDetails, ProductEntity> {

    @Override
    public ProductEntity convert(ImmutableProductCreateDetails immutableProductCreateRequest) {
        ProductEntity product = new ProductEntity();

        product.setName(immutableProductCreateRequest.getName());
        product.setProductNumber(immutableProductCreateRequest.getProductNumber());
        product.setInfo(immutableProductCreateRequest.getInfo());
        product.setCategory(immutableProductCreateRequest.getCategory());
        product.setPrice(immutableProductCreateRequest.getPrice());
        product.setAmount(immutableProductCreateRequest.getAmount());
        product.setLastAmountUpdate(immutableProductCreateRequest.getLastUpdateTime());

        return product;
    }
}