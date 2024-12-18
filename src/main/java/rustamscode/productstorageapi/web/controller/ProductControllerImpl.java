package rustamscode.productstorageapi.web.controller;

import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rustamscode.productstorageapi.exception.NonUniqueProductNumberException;
import rustamscode.productstorageapi.exception.ProductNotFoundException;
import rustamscode.productstorageapi.search.criteria.SearchCriteria;
import rustamscode.productstorageapi.service.ProductService;
import rustamscode.productstorageapi.service.dto.ImmutableProductCreateDetails;
import rustamscode.productstorageapi.service.dto.ImmutableProductUpdateDetails;
import rustamscode.productstorageapi.service.dto.ProductData;
import rustamscode.productstorageapi.web.dto.ProductCreateRequest;
import rustamscode.productstorageapi.web.dto.ProductDataResponse;
import rustamscode.productstorageapi.web.dto.ProductUpdateRequest;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing products in the storage.
 * Endpoints fot creating, reading, reading all, updating, and deleting products.
 */

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) //TODO Хорошо ли здесь использовать @Value?
public class ProductControllerImpl implements ProductController {
    /**
     * Service layer for product-management business logic
     */
    final ProductService productService;

    /**
     * Conversion service for data transfer object conversions
     */
    final ConversionService conversionService;

    /**
     * Creates a new product based on the provided request.
     *
     * @param productCreateRequest the request containing the details to create the product
     * @return the unique identifier of the created product
     * @throws IllegalArgumentException if the provided request is null
     * @throws ValidationException      if the provided request is not valid
     */
    @Override
    public UUID create(final ProductCreateRequest productCreateRequest) {
        return productService
                .create(conversionService.convert(productCreateRequest, ImmutableProductCreateDetails.class));
    }

    /**
     * Gets a product by its unique identifier.
     *
     * @param id the ID of the product get
     * @return the {@link ProductDataResponse} representation of the product
     * @throws IllegalArgumentException if the provided id is null
     * @throws ProductNotFoundException if no product is found with the given id
     */
    @Override
    public ProductDataResponse findById(final UUID id) {
        return conversionService.convert(productService.findById(id), ProductDataResponse.class);
    }

    /**
     * Gets a paginated list of all products.
     *
     * @param pageable the pagination information with default settings (page = 0, size = 10, sort = "name")
     * @return a {@link Page} of {@link ProductDataResponse} representing the products
     */
    @Override
    public Page<ProductDataResponse> findAll(final Pageable pageable) {
        return productService
                .findAll(pageable)
                .map(productData -> conversionService.convert(productData, ProductDataResponse.class));
    }

    /**
     * Updates an existing product with the provided details.
     *
     * @param id                   the ID of the product to be updated
     * @param productUpdateRequest the request containing the details to update the product
     * @return the ID of the updated product
     * @throws IllegalArgumentException        if the provided id or request is null
     * @throws ValidationException             if the provided request is not valid
     * @throws ProductNotFoundException        if no product is found with the given id
     * @throws NonUniqueProductNumberException if the new product number is not unique
     */
    @Override
    public UUID update(final UUID id, final ProductUpdateRequest productUpdateRequest) {
        return productService
                .update(id, conversionService.convert(productUpdateRequest, ImmutableProductUpdateDetails.class));
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to be deleted
     * @throws IllegalArgumentException if the provided id is null
     * @throws ProductNotFoundException if no product is found with the given id
     */
    @Override
    public void delete(final UUID id) {
        productService.delete(id);
    }

    /**
     * Searches for a product using criteria list.
     *
     * @param pageable     the pagination information
     * @param criteriaList the criteria list
     * @return a {@link Page} of {@link ProductData} representing the products
     */
    @Override
    public Page<ProductDataResponse> search(Pageable pageable, List<SearchCriteria> criteriaList) {
        return productService
                .search(pageable, criteriaList)
                .map(productData -> conversionService.convert(productData, ProductDataResponse.class));
    }
}
