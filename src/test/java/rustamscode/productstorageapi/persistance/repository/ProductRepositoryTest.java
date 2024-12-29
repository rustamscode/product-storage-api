package rustamscode.productstorageapi.persistance.repository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import rustamscode.productstorageapi.motherobject.ObjectMother;
import rustamscode.productstorageapi.persistance.entity.ProductEntity;
import rustamscode.productstorageapi.persistance.enumeration.Category;
import rustamscode.productstorageapi.search.criteria.BigDecimalSearchCriteria;
import rustamscode.productstorageapi.search.criteria.SearchCriteria;
import rustamscode.productstorageapi.search.criteria.StringSearchCriteria;
import rustamscode.productstorageapi.search.specification.ProductSpecification;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles(value = "test")
@FieldDefaults(level = AccessLevel.PRIVATE)
class ProductRepositoryTest extends RepositoryTest {

  @Autowired
  ProductRepository underTest;

  ProductEntity expectedProduct1;
  ProductEntity expectedProduct2;
  ProductEntity expectedProduct3;

  @BeforeEach
  void setup() {
    expectedProduct1 = ObjectMother.productEntity()
            .withName("Product1")
            .withProductNumber(BigInteger.valueOf(1111))
            .withPrice(BigDecimal.valueOf(1111))
            .withCategory(Category.BOOKS)
            .build();
    expectedProduct2 = ObjectMother.productEntity()
            .withName("Product2")
            .withProductNumber(BigInteger.valueOf(2222))
            .withPrice(BigDecimal.valueOf(2222))
            .withCategory(Category.FOOD)
            .build();
    expectedProduct3 = ObjectMother.productEntity()
            .withName("Product3")
            .withProductNumber(BigInteger.valueOf(3333))
            .withPrice(BigDecimal.valueOf(3333))
            .withCategory(Category.ELECTRONICS)
            .build();
  }

  @Override
  Object getRepository() {
    return underTest;
  }

  @Test
  void whenSaveThenFindById() {
    underTest.save(expectedProduct1);
    underTest.save(expectedProduct2);

    final Optional<ProductEntity> actualProduct1 = underTest.findByProductNumber(BigInteger.valueOf(1111));
    final Optional<ProductEntity> actualProduct2 = underTest.findByProductNumber(BigInteger.valueOf(2222));

    assertTrue(actualProduct1.isPresent());
    assertTrue(actualProduct2.isPresent());
    assertEquals("Product1", actualProduct1.get().getName());
    assertEquals("Product2", actualProduct2.get().getName());
  }

  @Test
  void whenNotSaveReturnNull() {
    final Optional<ProductEntity> actualProduct1 = underTest.findByProductNumber(BigInteger.valueOf(1111));
    final Optional<ProductEntity> actualProduct2 = underTest.findByProductNumber(BigInteger.valueOf(2222));

    assertThat(actualProduct1).isEmpty();
    assertThat(actualProduct2).isEmpty();
  }

  @Test
  void whenSaveThenExistsByProductNumber() {
    underTest.save(expectedProduct1);
    underTest.save(expectedProduct2);

    final boolean expectedProduct1Exists = underTest.existsByProductNumber(BigInteger.valueOf(1111));
    final boolean expectedProduct2Exists = underTest.existsByProductNumber(BigInteger.valueOf(2222));

    assertThat(expectedProduct1Exists).isTrue();
    assertThat(expectedProduct2Exists).isTrue();
  }

  @Test
  void whenSaveAllThenFindAllReturnAll() {
    underTest.save(expectedProduct1);
    underTest.save(expectedProduct2);
    underTest.save(expectedProduct3);

    final List<ProductEntity> actual = underTest.findAll();

    assertThat(actual).isNotEmpty();
    assertThat(actual)
            .anySatisfy(product -> {
              assertEquals(BigInteger.valueOf(1111), product.getProductNumber());
              assertEquals(Category.BOOKS, product.getCategory());
            })
            .anySatisfy(product -> {
              assertEquals(BigInteger.valueOf(2222), product.getProductNumber());
              assertEquals(Category.FOOD, product.getCategory());
            })
            .anySatisfy(product -> {
              assertEquals(BigInteger.valueOf(3333), product.getProductNumber());
              assertEquals(Category.ELECTRONICS, product.getCategory());
            });
  }

  @Test
  void whenDeleteProductNotExists() {
    underTest.save(expectedProduct1);
    underTest.save(expectedProduct2);

    final Optional<ProductEntity> actualProduct1 = underTest.findByProductNumber(BigInteger.valueOf(1111));
    final Optional<ProductEntity> actualProduct2 = underTest.findByProductNumber(BigInteger.valueOf(2222));
    underTest.deleteById(actualProduct1.get().getId());
    underTest.deleteById(actualProduct2.get().getId());
    final Optional<ProductEntity> actualDeletedProduct1 = underTest.findByProductNumber(BigInteger.valueOf(1111));
    final Optional<ProductEntity> actualDeletedProduct2 = underTest.findByProductNumber(BigInteger.valueOf(2222));

    assertThat(actualDeletedProduct1).isEmpty();
    assertThat(actualDeletedProduct2).isEmpty();
  }

  @Test
  void whenSearchReturnsPageProductsByStringCriteria() {
    underTest.save(expectedProduct1);
    underTest.save(expectedProduct2);
    underTest.save(expectedProduct3);
    final ProductSpecification productSpecification = new ProductSpecification();
    final List<SearchCriteria> criteriaList = List.of(
            StringSearchCriteria.builder()
                    .field("name")
                    .value("Product")
                    .operationType("LIKE")
                    .build()
    );
    final Pageable pageable = PageRequest.of(0, 5);
    final Specification<ProductEntity> specification = productSpecification.generateSpecification(criteriaList);

    final Page<ProductEntity> actual = underTest.findAll(specification, pageable);

    assertThat(actual).isNotEmpty();
    assertThat(actual.getContent().size()).isEqualTo(3);
    assertThat(actual)
            .anySatisfy(product -> {
              assertEquals(BigInteger.valueOf(1111), product.getProductNumber());
              assertEquals(Category.BOOKS, product.getCategory());
            })
            .anySatisfy(product -> {
              assertEquals(BigInteger.valueOf(2222), product.getProductNumber());
              assertEquals(Category.FOOD, product.getCategory());
            })
            .anySatisfy(product -> {
              assertEquals(BigInteger.valueOf(3333), product.getProductNumber());
              assertEquals(Category.ELECTRONICS, product.getCategory());
            });
  }

  @Test
  void whenSearchReturnsPageProductsByStringAndBigDecimalCriteria() {
    underTest.save(expectedProduct1);
    underTest.save(expectedProduct2);
    underTest.save(expectedProduct3);
    final ProductSpecification productSpecification = new ProductSpecification();
    final List<SearchCriteria> criteriaList = List.of(
            StringSearchCriteria.builder()
                    .field("name")
                    .value("Product")
                    .operationType("~")
                    .build(),
            BigDecimalSearchCriteria.builder()
                    .field("price")
                    .value(BigDecimal.valueOf(1500))
                    .operationType(">=")
                    .build()
    );
    final Pageable pageable = PageRequest.of(0, 5);
    final Specification<ProductEntity> specification = productSpecification.generateSpecification(criteriaList);

    final Page<ProductEntity> actual = underTest.findAll(specification, pageable);

    assertThat(actual).isNotEmpty();
    assertThat(actual.getContent().size()).isEqualTo(2);
    assertThat(actual)
            .anySatisfy(product -> {
              assertEquals(BigInteger.valueOf(2222), product.getProductNumber());
              assertEquals(Category.FOOD, product.getCategory());
            })
            .anySatisfy(product -> {
              assertEquals(BigInteger.valueOf(3333), product.getProductNumber());
              assertEquals(Category.ELECTRONICS, product.getCategory());
            });
  }
}
