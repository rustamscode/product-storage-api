package rustamscode.productstorageapi.service;

import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import rustamscode.productstorageapi.exception.NonUniqueProductNumberException;
import rustamscode.productstorageapi.exception.ProductNotFoundException;
import rustamscode.productstorageapi.persistance.entity.product.ProductEntity;
import rustamscode.productstorageapi.persistance.repository.ProductRepository;
import rustamscode.productstorageapi.service.dto.ImmutableProductCreateDetails;
import rustamscode.productstorageapi.service.dto.ImmutableProductFilterDetails;
import rustamscode.productstorageapi.service.dto.ImmutableProductUpdateDetails;
import rustamscode.productstorageapi.service.dto.ProductData;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service class for product-management business logic.
 * Provides methods for creating, reading, updating, and deleting products.
 */

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductServiceImpl implements ProductService {

    /**
     * Repository for accessing product data in the database.
     */
    final ProductRepository productRepository;

    /**
     * Service for converting between DTOs and entity objects.
     */
    final ConversionService conversionService;

    /**
     * Creates a new product based on the provided details.
     *
     * @param request the details to create the product
     * @return the unique identifier of the created product
     * @throws IllegalArgumentException        if the provided request is null
     * @throws NonUniqueProductNumberException if the product number is not unique
     */
    @Override
    public UUID create(final ImmutableProductCreateDetails request) {
        Assert.notNull(request, "Request must not be null");

        final ProductEntity product = conversionService.convert(request, ProductEntity.class);
        final BigInteger productNumber = product.getProductNumber();

        productRepository.findByProductNumber(productNumber)
                .ifPresent(p -> {
                    throw new NonUniqueProductNumberException(productNumber, p.getId());
                });

        return productRepository.save(product).getId();
    }

    /**
     * Gets a product by its ID.
     *
     * @param id the ID of the product to get
     * @return the {@link ProductData} representation of the product
     * @throws IllegalArgumentException if the provided id is null
     * @throws ProductNotFoundException if no product is found with the given id
     */
    @Override
    public ProductData findById(final UUID id) {
        Assert.notNull(id, "Id must not be null");

        final ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return conversionService.convert(product, ProductData.class);
    }

    /**
     * Gets a paginated list of all products.
     *
     * @param pageable the pagination information
     * @return a {@link Page} of {@link ProductData} representing the products
     */
    public Page<ProductData> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> conversionService.convert(product, ProductData.class));
    }

    /**
     * Updates an existing product with the provided details.
     *
     * @param id      the ID of the product to be updated
     * @param request the details to update the product with
     * @return the ID of the updated product
     * @throws IllegalArgumentException        if the provided id or request is null
     * @throws ProductNotFoundException        if no product is found with the given id
     * @throws NonUniqueProductNumberException if the new product number is not unique
     */
    @Override
    public UUID update(final UUID id, final ImmutableProductUpdateDetails request) {
        Assert.notNull(id, "Id must not be null");
        Assert.notNull(request, "Request must not be null");

        ProductEntity product = productRepository.findByIdLocked(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        final BigInteger newProductNumber = request.getProductNumber();

        if (productRepository.existsByProductNumber(newProductNumber)
                && !newProductNumber.equals(product.getProductNumber())) {
            throw new NonUniqueProductNumberException(newProductNumber);
        }

        if (product.getAmount().compareTo(request.getAmount()) != 0) {
            product.setLastAmountUpdate(LocalDateTime.now());
        }

        product.setProductNumber(request.getProductNumber());
        product.setInfo(request.getInfo());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setAmount(request.getAmount());

        productRepository.save(product);
        log.info("Product updated with id: {}", id);
        return id;
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
        Assert.notNull(id, "Id must not be null");

        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        productRepository.deleteById(id);
        log.info("Product deleted with id: {}", id);
    }

    @Override
    public Page<ProductData> searchProducts(final ImmutableProductFilterDetails filterDetails) {
        final PageRequest pageRequest = PageRequest.of(filterDetails.getPage(), filterDetails.getSize());

        final Specification<ProductEntity> specification = (root, query, criteriaBuilder) ->
        {
            final List<Predicate> predicates = new ArrayList<>();

            if (filterDetails.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), filterDetails.getName()));
            }
            if (filterDetails.getPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), filterDetails.getPrice()));
            }
            if (filterDetails.getAmount() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), filterDetails.getAmount()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return productRepository.findAll(specification, pageRequest)
                .map(product -> conversionService.convert(product, ProductData.class));

    }


}
