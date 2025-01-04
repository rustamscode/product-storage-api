package rustamscode.productstorageapi.web.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rustamscode.productstorageapi.enumeration.OrderStatus;
import rustamscode.productstorageapi.service.OrderService;
import rustamscode.productstorageapi.service.dto.ImmutableOrderedProductObject;
import rustamscode.productstorageapi.service.dto.ImmutableProductOrderDetails;
import rustamscode.productstorageapi.web.dto.request.OrderedProductRequest;
import rustamscode.productstorageapi.web.dto.request.ProductOrderRequest;
import rustamscode.productstorageapi.web.dto.response.OrderDataResponse;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {
  final OrderService orderService;
  final ConversionService conversionService;

  @Override
  public UUID create(final Long customerId, final ProductOrderRequest request) {
    return orderService.create(
        customerId,
        conversionService.convert(request, ImmutableProductOrderDetails.class)
    );
  }

  @Override
  public UUID update(final UUID id, final Long customerId, final List<OrderedProductRequest> orderedProducts) {
    final List<ImmutableOrderedProductObject> immutableOrderedProducts = orderedProducts.stream()
        .map(
            orderedProduct ->
                conversionService.convert(orderedProduct, ImmutableOrderedProductObject.class)
        )
        .collect(Collectors.toList());

    return orderService.update(id, customerId, immutableOrderedProducts);
  }

  @Override
  public OrderDataResponse findById(final UUID id, final Long customerId) {
    return conversionService.convert(orderService.findById(id, customerId), OrderDataResponse.class);
  }

  @Override
  public void delete(final UUID id, final Long customerId) {
    orderService.delete(id, customerId);
  }

  @Override
  public void confirm(final UUID id, final Long customerId) {
    //TODO Implementation
  }

  @Override
  public UUID updateStatus(final UUID id, final Long customerId, final OrderStatus status) {
    return orderService.updateStatus(id, customerId, status);
  }
}
