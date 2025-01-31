package rustamscode.productstorageapi.service;

import rustamscode.productstorageapi.service.dto.OrderData;
import rustamscode.productstorageapi.service.dto.OrderStatusChangeInfo;
import rustamscode.productstorageapi.service.dto.OrderedProductInfo;

import java.util.List;
import java.util.UUID;

public interface OrderService {

  UUID create(final Long customerId,
              final String address,
              final List<OrderedProductInfo> products);

  UUID update(final UUID orderId,
              final Long customerId,
              final List<OrderedProductInfo> orderedProductObjects);

  OrderData findById(final UUID id,
                     final Long customerId);

  void delete(final UUID id,
              final Long customerId);

  void confirm(final UUID id,
               final Long customerId);

  UUID updateStatus(final UUID id,
                    final Long customerId,
                    final OrderStatusChangeInfo status);

}
