package rustamscode.productstorageapi.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rustamscode.productstorageapi.persistance.entity.OrderedProductEntity;

import java.util.UUID;

@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProductEntity, UUID> {
}
