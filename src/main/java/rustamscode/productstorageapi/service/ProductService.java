package rustamscode.productstorageapi.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rustamscode.productstorageapi.service.dto.ImmutableProductCreateDetails;
import rustamscode.productstorageapi.service.dto.ImmutableProductUpdateDetails;
import rustamscode.productstorageapi.service.dto.ProductData;

import java.util.UUID;

public interface ProductService {
    UUID create(@Valid ImmutableProductCreateDetails request);

    ProductData findById(UUID id);

    Page<ProductData> findAll(Pageable pageable);

    UUID update(UUID id, ImmutableProductUpdateDetails request);

    void delete(UUID id);
}
