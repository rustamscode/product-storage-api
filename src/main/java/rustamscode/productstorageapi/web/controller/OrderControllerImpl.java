package rustamscode.productstorageapi.web.controller;

import lombok.RequiredArgsConstructor;
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

/**
 * REST Controller for managing orders.
 *
 * <p>This controller provides endpoints for creating, updating, retrieving,
 * and deleting orders.</p>
 */

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {
  /**
   * Service for managing orders and performing business logic.
   */
  private final OrderService orderService;

  /**
   * Conversion service for mapping request and response objects.
   */
  private final ConversionService conversionService;

  /**
   * Creates a new order for a specific customer.
   *
   * @param customerId The ID of the customer placing the order.
   * @param request    The request object containing order details.
   * @return The ID of the created order.
   */
  @Override
  public UUID create(final Long customerId, final ProductOrderRequest request) {
    return orderService.create(
        customerId,
        conversionService.convert(request, ImmutableProductOrderDetails.class)
    );
  }

  /**
   * Updates an existing order by modifying or adding ordered products.
   *
   * @param id              The ID of the order to update.
   * @param customerId      The ID of the customer associated with the order.
   * @param orderedProducts The list of updated or new ordered products.
   * @return The ID of the updated order.
   */
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

  /**
   * Finds an order by its ID and verifies customer ownership.
   *
   * @param id         The ID of the order to retrieve.
   * @param customerId The ID of the customer associated with the order.
   * @return The response object containing detailed order information.
   */
  @Override
  public OrderDataResponse findById(final UUID id, final Long customerId) {
    return conversionService.convert(orderService.findById(id, customerId), OrderDataResponse.class);
  }

  /**
   * Deletes an order by its ID after verifying customer ownership and business rules.
   *
   * @param id         The ID of the order to delete.
   * @param customerId The ID of the customer associated with the order.
   */
  @Override
  public void delete(final UUID id, final Long customerId) {
    orderService.delete(id, customerId);
  }

  @Override
  public void confirm(final UUID id, final Long customerId) {
    //TODO Implementation
  }

  /**
   * Updates the status of an order.
   *
   * @param id         The ID of the order.
   * @param customerId The ID of the customer associated with the order.
   * @param status     The new status to set for the order.
   * @return The ID of the order with the updated status.
   */
  @Override
  public UUID updateStatus(final UUID id, final Long customerId, final OrderStatus status) {
    return orderService.updateStatus(id, customerId, status);
  }
}
