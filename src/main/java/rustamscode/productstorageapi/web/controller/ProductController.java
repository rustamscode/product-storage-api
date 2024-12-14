package rustamscode.productstorageapi.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rustamscode.productstorageapi.web.dto.ProductCreateRequest;
import rustamscode.productstorageapi.web.dto.ProductDataResponse;
import rustamscode.productstorageapi.web.dto.ProductUpdateRequest;

import java.util.UUID;

public interface ProductController {
    UUID create(@Valid @NotNull ProductCreateRequest productCreateRequest);

    ProductDataResponse findById(UUID id);

    Page<ProductDataResponse> findAll(Pageable pageable);

    UUID update(UUID id, @Valid @NotNull ProductUpdateRequest productUpdateRequest);

    void delete(UUID id);
}
