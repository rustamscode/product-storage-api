package rustamscode.productstorageapi.service;

import org.springframework.transaction.annotation.Transactional;
import rustamscode.productstorageapi.enumeration.OrderStatus;
import rustamscode.productstorageapi.service.dto.ImmutableOrderedProductObject;
import rustamscode.productstorageapi.service.dto.ImmutableProductOrderDetails;
import rustamscode.productstorageapi.service.dto.OrderData;

import java.util.List;
import java.util.UUID;

public interface OrderService {

  @Transactional
  UUID create(final Long customerId,
              final ImmutableProductOrderDetails immutableProductOrderDetails);

  @Transactional
  UUID update(final UUID orderId,
              final Long customerId,
              final List<ImmutableOrderedProductObject> orderedProductObjects);

  @Transactional
  OrderData findById(final UUID id,
                     final Long customerId);

  @Transactional
  void delete(final UUID id,
              final Long customerId);

  void confirm(final UUID id,
               final Long customerId);

  @Transactional
  UUID updateStatus(final UUID id,
                    final Long customerId,
                    final OrderStatus status);

}
