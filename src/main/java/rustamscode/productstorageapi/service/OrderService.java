package rustamscode.productstorageapi.service;

import org.springframework.transaction.annotation.Transactional;
import rustamscode.productstorageapi.service.dto.ImmutableProductOrderDetails;

import java.util.UUID;

public interface OrderService {

  @Transactional
  UUID create(final Long customer_id,
              final ImmutableProductOrderDetails immutableProductOrderDetails);

}
