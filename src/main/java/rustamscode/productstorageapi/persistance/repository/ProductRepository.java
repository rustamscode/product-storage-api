package rustamscode.productstorageapi.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rustamscode.productstorageapi.persistance.entity.ProductEntity;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {
  boolean existsByProductNumber(BigInteger productNumber);

  Optional<ProductEntity> findByProductNumber(BigInteger productNumber);

  @Query(value = """
      SELECT * FROM products
      WHERE id = :id
      FOR UPDATE;
      """, nativeQuery = true)
  Optional<ProductEntity> findByIdLocked(@Param("id") UUID id);
}