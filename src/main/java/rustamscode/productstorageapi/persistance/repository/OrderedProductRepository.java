package rustamscode.productstorageapi.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rustamscode.productstorageapi.persistance.entity.OrderedProductEntity;
import rustamscode.productstorageapi.service.dto.OrderedProductDataObject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProductEntity, UUID> {

  @Query("""
    SELECT new rustamscode.productstorageapi.service.dto.OrderedProductDataObject(
        op.product.id,
        p.name,
        op.orderedProductAmount,
        op.orderedProductPrice
    )
    FROM OrderedProductEntity op
    JOIN op.product p
    WHERE op.order.id = :orderId
    """)
  List<OrderedProductDataObject> findAllByOrderId(@Param(value = "orderId") final UUID orderId);
}
