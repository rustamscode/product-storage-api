package rustamscode.productstorageapi.service.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ImmutableProductOrderDetails {

  String deliveryAddress;
  List<OrderedProductInfo> products;
}
