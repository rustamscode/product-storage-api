package rustamscode.productstorageapi.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import rustamscode.productstorageapi.exception.NonUniqueProductNumberException;
import rustamscode.productstorageapi.exception.ProductNotFoundException;
import rustamscode.productstorageapi.motherobject.ObjectMother;
import rustamscode.productstorageapi.persistance.entity.ProductEntity;
import rustamscode.productstorageapi.persistance.repository.ProductRepository;
import rustamscode.productstorageapi.search.criteria.SearchCriteria;
import rustamscode.productstorageapi.search.criteria.StringSearchCriteria;
import rustamscode.productstorageapi.service.dto.ImmutableProductCreateDetails;
import rustamscode.productstorageapi.service.dto.ImmutableProductUpdateDetails;
import rustamscode.productstorageapi.service.dto.ProductData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class ProductServiceTest {

    @Mock
    ProductRepository productRepositoryMock;

    @Mock
    ConversionService conversionServiceMock;

    @InjectMocks
    ProductServiceImpl underTest;

    ProductEntity product;
    UUID productId;

    @BeforeEach
    void setup() {
        productId = UUID.randomUUID();
        product = ObjectMother.productEntity()
                .withId(productId)
                .build();
    }

    @Test
    void createProductSuccess() {
        final ImmutableProductCreateDetails request = mock(ImmutableProductCreateDetails.class);
        when(conversionServiceMock.convert(request, ProductEntity.class)).thenReturn(product);
        when(productRepositoryMock.save(product)).thenReturn(product);

        final UUID result = underTest.create(request);

        assertEquals(productId, result);
        verify(productRepositoryMock).save(product);
    }

    @Test
    void createProductThrowsNonUniqueProductNumberException() {
        final ImmutableProductCreateDetails request = mock(ImmutableProductCreateDetails.class);
        when(conversionServiceMock.convert(request, ProductEntity.class)).thenReturn(product);
        when(productRepositoryMock.findByProductNumber(product.getProductNumber()))
                .thenReturn(Optional.ofNullable(product));

        assertThrows(NonUniqueProductNumberException.class, () -> underTest.create(request));
        verify(productRepositoryMock, never()).save(any());
    }

    @Test
    void getAllPagedProductsSuccess() {
        final Pageable pageable = PageRequest.of(0, 1);
        final Page<ProductEntity> productPage = new PageImpl<>(List.of(product), pageable, 1);
        final ProductData productData = ProductData.builder()
                .id(productId)
                .name(product.getName())
                .build();

        when(productRepositoryMock.findAll(pageable)).thenReturn(productPage);
        when(conversionServiceMock.convert(product, ProductData.class)).thenReturn(productData);

        final Page<ProductData> allProducts = underTest.findAll(pageable);

        assertEquals(1, allProducts.getTotalElements());
        assertThat(allProducts)
                .anySatisfy(product -> {
                    assertEquals("Product Test", product.getName());
                    assertEquals(productId, product.getId());
                });

        verify(productRepositoryMock).findAll(pageable);
        verify(conversionServiceMock).convert(product, ProductData.class);
    }

    @Test
    void readProductSuccess() {
        when(productRepositoryMock.findById(productId)).thenReturn(Optional.of(product));
        final ProductData productData = mock(ProductData.class);
        when(conversionServiceMock.convert(product, ProductData.class)).thenReturn(productData);

        final ProductData result = underTest.findById(productId);

        assertEquals(productData, result);
        verify(productRepositoryMock).findById(productId);
    }

    @Test
    void readProductThrowsProductNotFoundException() {
        when(productRepositoryMock.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> underTest.findById(productId));
    }

    @Test
    void updateProductSuccess() {
        final ImmutableProductUpdateDetails request = ImmutableProductUpdateDetails.builder()
                .productNumber(product.getProductNumber())
                .info(product.getInfo())
                .category(product.getCategory())
                .price(product.getPrice())
                .amount(BigDecimal.valueOf(1234))
                .build();
        when(productRepositoryMock.findByIdLocked(productId)).thenReturn(Optional.of(product));
        when(productRepositoryMock.save(product)).thenReturn(product);

        final UUID result = underTest.update(productId, request);

        assertEquals(productId, result);
        verify(productRepositoryMock).save(product);
    }

    @Test
    void updateProductThrowsProductNotFoundException() {
        final ImmutableProductUpdateDetails request = mock(ImmutableProductUpdateDetails.class);
        when(productRepositoryMock.findByIdLocked(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> underTest.update(productId, request));
    }

    @Test
    void deleteProductSuccess() {
        when(productRepositoryMock.existsById(productId)).thenReturn(true);

        underTest.delete(productId);

        verify(productRepositoryMock).deleteById(productId);
    }

    @Test
    void deleteProductThrowsProductNotFoundException() {
        when(productRepositoryMock.existsById(productId)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> underTest.delete(productId));
        verify(productRepositoryMock, never()).deleteById(any());
    }

    @Test
    void searchProductByStringCriteriaSuccess() {
        final ProductData productData = ProductData.builder()
                .id(productId)
                .name(product.getName())
                .build();
        final List<SearchCriteria> criteriaList = List.of(
                StringSearchCriteria.builder()
                        .field("name")
                        .value("Product")
                        .operationType("~")
                        .build());
        final Pageable pageable = PageRequest.of(0, 1);
        final Page<ProductEntity> productPage = new PageImpl<>(List.of(product), pageable, 1);

        when(productRepositoryMock.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);
        when(conversionServiceMock.convert(product, ProductData.class)).thenReturn(productData);

        final Page<ProductData> filteredProducts = underTest.search(pageable, criteriaList);

        assertEquals(1, filteredProducts.getTotalElements());
        assertThat(filteredProducts)
                .anySatisfy(product -> {
                    assertEquals("Product Test", product.getName());
                    assertEquals(productId, product.getId());
                });

        verify(productRepositoryMock).findAll(any(Specification.class), eq(pageable));
        verify(conversionServiceMock).convert(product, ProductData.class);
    }


}
