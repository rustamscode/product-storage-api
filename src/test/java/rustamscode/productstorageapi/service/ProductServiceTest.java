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
import rustamscode.productstorageapi.persistance.entity.product.Category;
import rustamscode.productstorageapi.service.dto.ImmutableProductCreateDetails;
import rustamscode.productstorageapi.service.dto.ImmutableProductUpdateDetails;
import rustamscode.productstorageapi.service.dto.ProductData;
import rustamscode.productstorageapi.persistance.entity.product.ProductEntity;
import rustamscode.productstorageapi.exception.NonUniqueProductNumberException;
import rustamscode.productstorageapi.exception.ProductNotFoundException;
import rustamscode.productstorageapi.persistance.repository.ProductRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ConversionService conversionService;

    @InjectMocks
    ProductServiceImpl productService;

    ProductEntity product;
    UUID productId;

    @BeforeEach
    void setup() {
        productId = UUID.randomUUID();
        product = new ProductEntity();
        product.setId(productId);
        product.setName("TestProduct");
        product.setProductNumber(BigInteger.valueOf(1111));
        product.setInfo("TestProduct Info");
        product.setCategory(Category.BOOKS);
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setAmount(BigDecimal.valueOf(30));
        product.setLastAmountUpdate(LocalDateTime.now());
    }

    @Test
    void createProductSuccess() {
        ImmutableProductCreateDetails request = mock(ImmutableProductCreateDetails.class);
        when(conversionService.convert(request, ProductEntity.class)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        UUID result = productService.create(request);

        assertEquals(productId, result);
        verify(productRepository).save(product);
    }

    @Test
    void createProductThrowsNonUniqueProductNumberException() {
        ImmutableProductCreateDetails request = mock(ImmutableProductCreateDetails.class);
        when(conversionService.convert(request, ProductEntity.class)).thenReturn(product);
        when(productRepository.findByProductNumber(product.getProductNumber()))
                .thenReturn(Optional.ofNullable(product));

        assertThrows(NonUniqueProductNumberException.class, () -> productService.create(request));
        verify(productRepository, never()).save(any());
    }

    @Test
    void getAllPagedProductsSuccess() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<ProductEntity> productPage = new PageImpl<>(List.of(product), pageable, 1);

        ProductData productData = ProductData.builder()
                .id(productId)
                .name(product.getName())
                .build();

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(conversionService.convert(product, ProductData.class)).thenReturn(productData);

        Page<ProductData> result = productService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("TestProduct", result.getContent().getFirst().getName());

        verify(productRepository).findAll(pageable);
        verify(conversionService).convert(product, ProductData.class);
    }

    @Test
    void readProductSuccess() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        ProductData productData = mock(ProductData.class);
        when(conversionService.convert(product, ProductData.class)).thenReturn(productData);

        ProductData result = productService.findById(productId);

        assertEquals(productData, result);
        verify(productRepository).findById(productId);
    }

    @Test
    void readProductThrowsProductNotFoundException() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(productId));
    }

    @Test
    void updateProductSuccess() {
        ImmutableProductUpdateDetails request = ImmutableProductUpdateDetails.builder()
                .productNumber(product.getProductNumber())
                .info(product.getInfo())
                .category(product.getCategory())
                .price(product.getPrice())
                .amount(BigDecimal.valueOf(2221))
                .build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        UUID result = productService.update(productId, request);

        assertEquals(productId, result);
        verify(productRepository).save(product);
    }

    @Test
    void updateProductThrowsProductNotFoundException() {
        ImmutableProductUpdateDetails request = mock(ImmutableProductUpdateDetails.class);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.update(productId, request));
    }

    @Test
    void deleteProductSuccess() {
        when(productRepository.existsById(productId)).thenReturn(true);

        productService.delete(productId);

        verify(productRepository).deleteById(productId);
    }

    @Test
    void deleteProductThrowsProductNotFoundException() {
        when(productRepository.existsById(productId)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.delete(productId));
        verify(productRepository, never()).deleteById(any());
    }


}
