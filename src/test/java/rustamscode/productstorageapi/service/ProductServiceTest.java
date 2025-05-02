package rustamscode.productstorageapi.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import rustamscode.productstorageapi.enumeration.Currency;
import rustamscode.productstorageapi.exception.NonUniqueProductNumberException;
import rustamscode.productstorageapi.exception.ProductNotFoundException;
import rustamscode.productstorageapi.motherobject.ObjectMother;
import rustamscode.productstorageapi.persistance.entity.ProductEntity;
import rustamscode.productstorageapi.persistance.repository.ProductRepository;
import rustamscode.productstorageapi.search.criteria.SearchCriteria;
import rustamscode.productstorageapi.search.criteria.StringSearchCriteria;
import rustamscode.productstorageapi.search.specification.ProductSpecification;
import rustamscode.productstorageapi.service.dto.ImmutableProductCreateDetails;
import rustamscode.productstorageapi.service.dto.ImmutableProductUpdateDetails;
import rustamscode.productstorageapi.service.dto.ProductData;

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

@FieldDefaults(level = AccessLevel.PRIVATE)
class ProductServiceTest extends ServiceTest {

  @Mock
  ProductRepository productRepositoryMock;

  @Mock
  ConversionService conversionServiceMock;

  @Mock
  ProductSpecification productSpecification;

  @InjectMocks
  ProductServiceImpl underTest;

  ProductEntity expectedProduct;
  UUID expectedId;

  @BeforeEach
  void setup() {
    expectedId = UUID.randomUUID();
    expectedProduct = ObjectMother.productEntity()
            .withId(expectedId)
            .build();
  }

  @Override
  Object getService() {
    return underTest;
  }

  @Test
  void createProductSuccess() {
    final ImmutableProductCreateDetails immutableCreateDetailsMock = mock(ImmutableProductCreateDetails.class);

    when(conversionServiceMock.convert(immutableCreateDetailsMock, ProductEntity.class)).thenReturn(expectedProduct);
    when(productRepositoryMock.save(expectedProduct)).thenReturn(expectedProduct);
    final UUID actualId = underTest.create(immutableCreateDetailsMock);

    assertEquals(expectedId, actualId);
    verify(productRepositoryMock).save(expectedProduct);
  }

  @Test
  void createProductThrowsNonUniqueProductNumberException() {
    final ImmutableProductCreateDetails immutableCreateDetailsMock = mock(ImmutableProductCreateDetails.class);

    when(conversionServiceMock.convert(immutableCreateDetailsMock, ProductEntity.class)).thenReturn(expectedProduct);
    when(productRepositoryMock.findByProductNumber(expectedProduct.getProductNumber()))
            .thenReturn(Optional.ofNullable(expectedProduct));

    assertThrows(NonUniqueProductNumberException.class, () -> underTest.create(immutableCreateDetailsMock));
    verify(productRepositoryMock, never()).save(any());
  }

  @Test
  void getAllPagedProductsSuccess() {
    final Pageable pageable = PageRequest.of(0, 1);
    final Page<ProductEntity> productPage = new PageImpl<>(List.of(expectedProduct), pageable, 1);
    final ProductData productData = ProductData.builder()
            .id(expectedId)
            .name(expectedProduct.getName())
            .build();

    when(productRepositoryMock.findAll(pageable)).thenReturn(productPage);
    when(conversionServiceMock.convert(expectedProduct, ProductData.class)).thenReturn(productData);
    final Page<ProductData> actual = underTest.findAll(Currency.RUB, pageable);

    assertEquals(1, actual.getTotalElements());
    assertThat(actual)
            .anySatisfy(product -> {
              assertEquals("Product Test", product.getName());
              assertEquals(expectedId, product.getId());
            });
    verify(productRepositoryMock).findAll(pageable);
    verify(conversionServiceMock).convert(expectedProduct, ProductData.class);
  }

  @Test
  void readProductSuccess() {
    final ProductData expectedProductData = mock(ProductData.class);

    when(productRepositoryMock.findById(expectedId)).thenReturn(Optional.of(expectedProduct));
    when(conversionServiceMock.convert(expectedProduct, ProductData.class)).thenReturn(expectedProductData);
    final ProductData actualProductData = underTest.findById(Currency.RUB, expectedId);

    assertEquals(expectedProductData, actualProductData);
    verify(productRepositoryMock).findById(expectedId);
  }

  @Test
  void readProductThrowsProductNotFoundException() {
    when(productRepositoryMock.findById(expectedId)).thenReturn(Optional.empty());

    assertThrows(ProductNotFoundException.class, () -> underTest.findById(Currency.RUB, expectedId));
  }

  @Test
  void updateProductSuccess() {
    final ImmutableProductUpdateDetails immutableUpdateDetailsMock = ObjectMother.immutableProductUpdateDetails()
            .build();

    when(productRepositoryMock.findByIdLocked(expectedId)).thenReturn(Optional.of(expectedProduct));
    when(productRepositoryMock.save(expectedProduct)).thenReturn(expectedProduct);
    final UUID actualId = underTest.update(expectedId, immutableUpdateDetailsMock);

    assertEquals(expectedId, actualId);
    verify(productRepositoryMock).save(expectedProduct);
  }

  @Test
  void updateProductThrowsProductNotFoundException() {
    final ImmutableProductUpdateDetails request = mock(ImmutableProductUpdateDetails.class);
    when(productRepositoryMock.findByIdLocked(expectedId)).thenReturn(Optional.empty());

    assertThrows(ProductNotFoundException.class, () -> underTest.update(expectedId, request));
  }

  @Test
  void deleteProductSuccess() {
    when(productRepositoryMock.existsById(expectedId)).thenReturn(true);

    underTest.delete(expectedId);

    verify(productRepositoryMock).deleteById(expectedId);
  }

  @Test
  void deleteProductThrowsProductNotFoundException() {
    when(productRepositoryMock.existsById(expectedId)).thenReturn(false);

    assertThrows(ProductNotFoundException.class, () -> underTest.delete(expectedId));
    verify(productRepositoryMock, never()).deleteById(any());
  }

  @Test
  void searchProductByStringCriteriaSuccess() {
    final ProductData expectedProductData = ObjectMother.productData()
            .withId(expectedId)
            .withName(expectedProduct.getName())
            .build();
    final List<SearchCriteria> criteriaList = List.of(
            StringSearchCriteria.builder()
                    .field("name")
                    .value("Product")
                    .operationType("~")
                    .build());
    final Pageable pageable = PageRequest.of(0, 1);
    final Page<ProductEntity> expectedProductPage = new PageImpl<>(List.of(expectedProduct), pageable, 1);
    final Specification<ProductEntity> specificationMock = mock(Specification.class);

    when(productSpecification.generateSpecification(criteriaList)).thenReturn(specificationMock);
    when(productRepositoryMock.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedProductPage);
    when(conversionServiceMock.convert(expectedProduct, ProductData.class)).thenReturn(expectedProductData);
    final Page<ProductData> actualPage = underTest.search(Currency.RUB, pageable, criteriaList);

    assertEquals(1, actualPage.getTotalElements());
    assertThat(actualPage)
            .anySatisfy(product -> {
              assertEquals("Product Test", product.getName());
              assertEquals(expectedId, product.getId());
            });
    verify(productRepositoryMock).findAll(any(Specification.class), eq(pageable));
    verify(conversionServiceMock).convert(expectedProduct, ProductData.class);
  }
}
