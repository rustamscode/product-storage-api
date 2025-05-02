package rustamscode.productstorageapi.service.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
public class OrderedProductInfo {

  UUID id;

  BigDecimal amount;
}
