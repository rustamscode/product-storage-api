package rustamscode.productstorageapi.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.service.dto.ImmutableProductFilterDetails;
import rustamscode.productstorageapi.web.dto.ProductFilterRequest;

@Component
public class ProductFilterRequestToImmutableProductFilterDetailsConverter implements
        Converter<ProductFilterRequest, ImmutableProductFilterDetails> {
    @Override
    public ImmutableProductFilterDetails convert(ProductFilterRequest request) {
        return ImmutableProductFilterDetails.builder()
                .name(request.getName())
                .price(request.getPrice())
                .amount(request.getAmount())
                .page(request.getPage())
                .size(request.getSize())
                .build();

    }
}
