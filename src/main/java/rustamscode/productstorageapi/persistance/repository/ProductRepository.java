package rustamscode.productstorageapi.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rustamscode.productstorageapi.persistance.entity.product.ProductEntity;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    boolean existsByProductNumber(BigInteger productNumber);
    Optional<ProductEntity> findByProductNumber(BigInteger productNumber);
}