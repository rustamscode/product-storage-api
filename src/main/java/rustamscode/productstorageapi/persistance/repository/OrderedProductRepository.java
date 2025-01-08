package rustamscode.productstorageapi.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rustamscode.productstorageapi.persistance.entity.OrderedProductEntity;
import rustamscode.productstorageapi.persistance.entity.key.OrderedProductEntityKey;
import rustamscode.productstorageapi.persistance.projection.OrderedProductProjection;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProductEntity, OrderedProductEntityKey> {

  @Query("""
      SELECT new    rustamscode.productstorageapi.persistance.projection.OrderedProductProjection(
          op.product.id,
          p.name,
          op.amount,
          op.price
      )
      FROM OrderedProductEntity op
      JOIN op.product p
      WHERE op.order.id = :orderId
      """)
  List<OrderedProductProjection> findProjectionsByOrderId(@Param(value = "orderId") final UUID orderId);

  @Query("""
      SELECT op FROM OrderedProductEntity op
      JOIN FETCH op.order o
      JOIN FETCH op.product p
      JOIN FETCH o.customer c
      WHERE op.id IN :ids
      """)
  List<OrderedProductEntity> findAllById(@Param(value = "ids") List<OrderedProductEntityKey> ids);
}
