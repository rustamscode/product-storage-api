package rustamscode.productstorageapi.conversion;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.enumeration.OrderStatus;
import rustamscode.productstorageapi.persistance.entity.OrderEntity;
import rustamscode.productstorageapi.service.dto.ImmutableProductOrderDetails;

@Component
@RequiredArgsConstructor
public class ImmutableProductOrderDetailsToOrderEntityConverter
    implements Converter<ImmutableProductOrderDetails, OrderEntity> {

  @Override
  public OrderEntity convert(ImmutableProductOrderDetails details) {
    return OrderEntity.builder()
        .orderStatus(OrderStatus.CREATED)
        .deliveryAddress(details.getDeliveryAddress())
        .build();
  }
}