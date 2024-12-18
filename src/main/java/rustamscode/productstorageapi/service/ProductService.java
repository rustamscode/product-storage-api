package rustamscode.productstorageapi.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import rustamscode.productstorageapi.search.criteria.SearchCriteria;
import rustamscode.productstorageapi.service.dto.ImmutableProductCreateDetails;
import rustamscode.productstorageapi.service.dto.ImmutableProductUpdateDetails;
import rustamscode.productstorageapi.service.dto.ProductData;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    UUID create(@Valid final ImmutableProductCreateDetails request);

    ProductData findById(final UUID id);

    Page<ProductData> findAll(final Pageable pageable);

    @Transactional
    UUID update(final UUID id, final ImmutableProductUpdateDetails request);

    void delete(final UUID id);

    Page<ProductData> search(Pageable pageable, List<SearchCriteria> criteriaList);
}
