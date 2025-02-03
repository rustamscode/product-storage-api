package rustamscode.productstorageapi.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rustamscode.productstorageapi.persistance.entity.ProductEntity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {
  boolean existsByProductNumber(final BigInteger productNumber);

  Optional<ProductEntity> findByProductNumber(final BigInteger productNumber);

  @Query(value = """
      SELECT * FROM product
      WHERE id = :id
      FOR UPDATE;
      """, nativeQuery = true)
  Optional<ProductEntity> findByIdLocked(@Param("id") final UUID id);
}