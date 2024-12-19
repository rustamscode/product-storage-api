package rustamscode.productstorageapi.persistance.repository;

import jakarta.persistence.criteria.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import rustamscode.productstorageapi.persistance.entity.ProductEntity;
import rustamscode.productstorageapi.persistance.enumeration.Category;
import rustamscode.productstorageapi.search.criteria.BigDecimalSearchCriteria;
import rustamscode.productstorageapi.search.criteria.SearchCriteria;
import rustamscode.productstorageapi.search.criteria.StringSearchCriteria;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles(value = "test")
class ProductRepositoryTest {

    @Autowired
    ProductRepository underTest;

    ProductEntity product1;
    ProductEntity product2;
    ProductEntity product3;

    UUID productId1;
    UUID productId2;
    UUID productId3;

    @BeforeEach
    void setup() {
        productId1 = UUID.randomUUID();
        productId2 = UUID.randomUUID();
        productId3 = UUID.randomUUID();

        product1 = ProductEntity.builder()
                .name("Product1")
                .productNumber(BigInteger.valueOf(1111))
                .info("Product1 info")
                .category(Category.BOOKS)
                .price(BigDecimal.valueOf(1111))
                .build();
        product2 = ProductEntity.builder()
                .name("Product2")
                .productNumber(BigInteger.valueOf(2222))
                .info("Product2 info")
                .category(Category.FOOD)
                .price(BigDecimal.valueOf(2222))
                .build();
        product3 = ProductEntity.builder()
                .name("Product3")
                .productNumber(BigInteger.valueOf(3333))
                .info("Product3 info")
                .category(Category.ELECTRONICS)
                .price(BigDecimal.valueOf(3333))
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

        List<SearchCriteria> criteriaList = List.of(
                StringSearchCriteria.builder()
                        .field("name")
                        .value("Product")
                        .operationType("LIKE")
                        .build()
        );

        Pageable pageable = PageRequest.of(0, 5);
        Specification<ProductEntity> specification = (root, query, builder) -> {
            Predicate[] predicates = criteriaList.stream()
                    .map(criteria -> criteria.toPredicate(builder, root))
                    .toArray(Predicate[]::new);
            return builder.and(predicates);
        };

        Page<ProductEntity> filteredProducts = underTest.findAll(specification, pageable);

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

        List<SearchCriteria> criteriaList = List.of(
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

        Pageable pageable = PageRequest.of(0, 5);
        Specification<ProductEntity> specification = (root, query, builder) -> {
            Predicate[] predicates = criteriaList.stream()
                    .map(criteria -> criteria.toPredicate(builder, root))
                    .toArray(Predicate[]::new);
            return builder.and(predicates);
        };

        Page<ProductEntity> filteredProducts = underTest.findAll(specification, pageable);

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
