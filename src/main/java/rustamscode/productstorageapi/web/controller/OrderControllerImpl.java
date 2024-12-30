package rustamscode.productstorageapi.web.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rustamscode.productstorageapi.service.OrderService;
import rustamscode.productstorageapi.service.dto.ImmutableProductOrderDetails;
import rustamscode.productstorageapi.web.dto.request.ProductOrderRequest;

import java.util.UUID;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/order")
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
}
