package rustamscode.productstorageapi.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rustamscode.productstorageapi.persistance.entity.OrderEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

  @Query("""
      SELECT o FROM OrderEntity o
      JOIN FETCH o.orderedProducts op
      JOIN FETCH op.product
      JOIN FETCH o.customer c
      WHERE o.id = :id
      """)
  Optional<OrderEntity> findById(@Param(value = "id") final UUID id);
}
