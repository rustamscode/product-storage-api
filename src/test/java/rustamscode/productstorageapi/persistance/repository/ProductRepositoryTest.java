package rustamscode.productstorageapi.persistance.repository;

import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles(value = "test")
@FieldDefaults(level = AccessLevel.PRIVATE)
class ProductRepositoryTest {

    @Autowired
    ProductRepository underTest;

    ProductEntity product1;
    ProductEntity product2;
    ProductEntity product3;

    @BeforeEach
    void setup() {
        product1 = ObjectMother.productEntity()
                .withName("Product1")
                .withProductNumber(BigInteger.valueOf(1111))
                .withPrice(BigDecimal.valueOf(1111))
                .withCategory(Category.BOOKS)
                .build();
        product2 = ObjectMother.productEntity()
                .withName("Product2")
                .withProductNumber(BigInteger.valueOf(2222))
                .withPrice(BigDecimal.valueOf(2222))
                .withCategory(Category.FOOD)
                .build();
        product3 = ObjectMother.productEntity()
                .withName("Product3")
                .withProductNumber(BigInteger.valueOf(3333))
                .withPrice(BigDecimal.valueOf(3333))
                .withCategory(Category.ELECTRONICS)
                .build();
    }

    @Test
    void whenSaveThenFindById() {
        underTest.save(product1);
        underTest.save(product2);

        final Optional<ProductEntity> retrievedProduct1 = underTest.findByProductNumber(BigInteger.valueOf(1111));
        final Optional<ProductEntity> retrievedProduct2 = underTest.findByProductNumber(BigInteger.valueOf(2222));

        assertThat(retrievedProduct1).isPresent();
        assertThat(retrievedProduct2).isPresent();
        assertThat(retrievedProduct1.get().getName()).isEqualTo("Product1");
        assertThat(retrievedProduct2.get().getName()).isEqualTo("Product2");
    }

    @Test
    void whenNotSaveReturnNull() {
        final Optional<ProductEntity> retrievedProduct1 = underTest.findByProductNumber(BigInteger.valueOf(1111));
        final Optional<ProductEntity> retrievedProduct2 = underTest.findByProductNumber(BigInteger.valueOf(2222));

        assertThat(retrievedProduct1).isEmpty();
        assertThat(retrievedProduct2).isEmpty();
    }

    @Test
    void whenSaveThenExistsByProductNumber() {
        underTest.save(product1);
        underTest.save(product2);

        final boolean product1Exists = underTest.existsByProductNumber(BigInteger.valueOf(1111));
        final boolean product2Exists = underTest.existsByProductNumber(BigInteger.valueOf(2222));

        assertThat(product1Exists).isTrue();
        assertThat(product2Exists).isTrue();
    }

    @Test
    void whenSaveAllThenFindAllReturnAll() {
        underTest.save(product1);
        underTest.save(product2);
        underTest.save(product3);

        final List<ProductEntity> allProducts = underTest.findAll();

        assertThat(allProducts).isNotEmpty();
        assertThat(allProducts).anySatisfy(product -> {
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
        underTest.save(product1);
        underTest.save(product2);

        final Optional<ProductEntity> retrievedProduct1 = underTest.findByProductNumber(BigInteger.valueOf(1111));
        final Optional<ProductEntity> retrievedProduct2 = underTest.findByProductNumber(BigInteger.valueOf(2222));

        assertThat(retrievedProduct1).isPresent();
        assertThat(retrievedProduct2).isPresent();
        assertThat(retrievedProduct1.get().getName()).isEqualTo("Product1");
        assertThat(retrievedProduct2.get().getName()).isEqualTo("Product2");

        underTest.deleteById(retrievedProduct1.get().getId());
        underTest.deleteById(retrievedProduct2.get().getId());
        final Optional<ProductEntity> deletedProduct1 = underTest.findByProductNumber(BigInteger.valueOf(1111));
        final Optional<ProductEntity> deletedProduct2 = underTest.findByProductNumber(BigInteger.valueOf(2222));

        assertThat(deletedProduct1).isEmpty();
        assertThat(deletedProduct2).isEmpty();
    }

    @Test
    void whenSearchReturnsPageProductsByStringCriteria() {
        underTest.save(product1);
        underTest.save(product2);
        underTest.save(product3);

        final List<SearchCriteria> criteriaList = List.of(
                StringSearchCriteria.builder()
                        .field("name")
                        .value("Product")
                        .operationType("LIKE")
                        .build()
        );

        final Pageable pageable = PageRequest.of(0, 5);
        final Specification<ProductEntity> specification = (root, query, builder) -> {
            Predicate[] predicates = criteriaList.stream()
                    .map(criteria -> criteria.toPredicate(builder, root))
                    .toArray(Predicate[]::new);
            return builder.and(predicates);
        };

        final Page<ProductEntity> filteredProducts = underTest.findAll(specification, pageable);

        assertThat(filteredProducts).isNotEmpty();
        assertThat(filteredProducts.getContent().size()).isEqualTo(3);
        assertThat(filteredProducts)
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
        underTest.save(product1);
        underTest.save(product2);
        underTest.save(product3);

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
        final Specification<ProductEntity> specification = (root, query, builder) -> {
            Predicate[] predicates = criteriaList.stream()
                    .map(criteria -> criteria.toPredicate(builder, root))
                    .toArray(Predicate[]::new);
            return builder.and(predicates);
        };

        final Page<ProductEntity> filteredProducts = underTest.findAll(specification, pageable);

        assertThat(filteredProducts).isNotEmpty();
        assertThat(filteredProducts.getContent().size()).isEqualTo(2);
        assertThat(filteredProducts)
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
