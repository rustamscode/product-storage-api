package rustamscode.productstorageapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import rustamscode.productstorageapi.enumeration.OrderStatus;
import rustamscode.productstorageapi.exception.CustomerNotFoundException;
import rustamscode.productstorageapi.exception.IllegalOrderAccessException;
import rustamscode.productstorageapi.exception.InsufficientProductException;
import rustamscode.productstorageapi.exception.OrderAccessDeniedException;
import rustamscode.productstorageapi.exception.OrderNotFoundException;
import rustamscode.productstorageapi.exception.ProductNotFoundException;
import rustamscode.productstorageapi.exception.UnavailableProductException;
import rustamscode.productstorageapi.persistance.entity.CustomerEntity;
import rustamscode.productstorageapi.persistance.entity.OrderEntity;
import rustamscode.productstorageapi.persistance.entity.OrderedProductEntity;
import rustamscode.productstorageapi.persistance.entity.ProductEntity;
import rustamscode.productstorageapi.persistance.entity.key.OrderedProductEntityKey;
import rustamscode.productstorageapi.persistance.repository.CustomerRepository;
import rustamscode.productstorageapi.persistance.repository.OrderRepository;
import rustamscode.productstorageapi.persistance.repository.OrderedProductRepository;
import rustamscode.productstorageapi.persistance.repository.ProductRepository;
import rustamscode.productstorageapi.service.dto.ImmutableOrderedProductObject;
import rustamscode.productstorageapi.service.dto.ImmutableProductOrderDetails;
import rustamscode.productstorageapi.service.dto.OrderData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for managing orders in the system.
 * Provides methods for creation, updating, retrieval, and deletion of orders.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  /**
   * Repository for managing {@link OrderEntity}.
   */
  private final OrderRepository orderRepository;

  /**
   * Repository for managing {@link OrderedProductEntity}.
   */
  private final OrderedProductRepository orderedProductRepository;

  /**
   * Repository for managing {@link CustomerEntity}.
   */
  private final CustomerRepository customerRepository;

  /**
   * Repository for managing {@link ProductEntity}.
   */
  private final ProductRepository productRepository;

  /**
   * Conversion service for mapping between DTOs and entities.
   */
  private final ConversionService conversionService;

  /**
   * Creates a new order for a specific customer with the specified products.
   *
   * @param customerId                   The ID of the customer.
   * @param immutableProductOrderDetails DTO of the ordered product.
   * @return The ID of the created order.
   * @throws CustomerNotFoundException    If the customer does not exist.
   * @throws UnavailableProductException  If any product is unavailable.
   * @throws InsufficientProductException If the product stock is insufficient.
   */
  @Override
  public UUID create(final Long customerId,
                     final ImmutableProductOrderDetails immutableProductOrderDetails) {
    Assert.notNull(customerId, "Customer ID must not be null");
    Assert.notNull(immutableProductOrderDetails, "Order details must not be null");

    final List<ImmutableOrderedProductObject> orderedProductObjects = immutableProductOrderDetails.getProducts();

    final CustomerEntity customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new CustomerNotFoundException(customerId));

    final List<OrderedProductEntity> orderedProducts = mapToOrderedProducts(orderedProductObjects);
    final OrderEntity order = conversionService.convert(immutableProductOrderDetails, OrderEntity.class);
    order.setCustomer(customer);
    final OrderEntity savedOrder = orderRepository.save(order);
    log.info("Order was saved without products");

    orderedProducts.forEach(orderedProduct -> {
      orderedProduct.setOrder(savedOrder);
      orderedProduct.setId(new OrderedProductEntityKey(orderedProduct.getProduct().getId(), order.getId()));
    });
    orderedProductRepository.saveAll(orderedProducts);
    log.info("Ordered products were saved");

    savedOrder.setOrderedProducts(orderedProducts);
    orderRepository.save(savedOrder);
    log.info("Order with products was saved");

    return savedOrder.getId();
  }

  /**
   * Updates an existing order by adding or modifying the ordered products.
   *
   * @param id                    The ID of the order to update.
   * @param customerId            The ID of the customer associated with the order.
   * @param orderedProductObjects List of products to update or add to the order.
   * @return The ID of the updated order.
   * @throws OrderNotFoundException      If the order does not exist.
   * @throws CustomerNotFoundException   If the customer does not exist.
   * @throws OrderAccessDeniedException  If the customer does not own the order.
   * @throws IllegalOrderAccessException If the order status is not modifiable.
   */
  @Override
  public UUID update(final UUID id,
                     final Long customerId,
                     final List<ImmutableOrderedProductObject> orderedProductObjects) {
    Assert.notNull(id, "Order ID must not be null");
    Assert.notNull(customerId, "Customer ID must not be null");
    Assert.notNull(orderedProductObjects, "Ordered product list must not be null");

    final OrderEntity order = orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(id));
    customerRepository.findById(customerId)
        .orElseThrow(() -> new CustomerNotFoundException(customerId));

    if (!order.getCustomer().getId().equals(customerId)) {
      throw new OrderAccessDeniedException(id, customerId);
    }

    if (order.getOrderStatus() != OrderStatus.CREATED) {
      throw new IllegalOrderAccessException(order.getOrderStatus());
    }

    final List<OrderedProductEntity> updatedOrderedProducts = filterUpdatedOrderedProductsFromExisting(
        mapToOrderedProducts(orderedProductObjects), order
    );

    updatedOrderedProducts.forEach(orderedProduct -> {
      orderedProduct.setOrder(order);
      orderedProduct.setId(new OrderedProductEntityKey(orderedProduct.getProduct().getId(), order.getId()));
    });

    order.getOrderedProducts().addAll(updatedOrderedProducts);
    orderRepository.save(order);
    log.info("Updated order was saved");

    return id;
  }

  /**
   * Finds an order by its ID and verifies ownership by the customer.
   *
   * @param id         The ID of the order to retrieve.
   * @param customerId The ID of the customer.
   * @return The detailed order data.
   * @throws OrderNotFoundException     If the order does not exist.
   * @throws CustomerNotFoundException  If the customer does not exist.
   * @throws OrderAccessDeniedException If the customer does not own the order.
   */
  @Override
  public OrderData findById(final UUID id, final Long customerId) {
    Assert.notNull(id, "Order ID must not be null");
    Assert.notNull(customerId, "Customer ID must not be null");

    final OrderEntity order = orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(id));
    customerRepository.findById(customerId)
        .orElseThrow(() -> new CustomerNotFoundException(customerId));

    if (!order.getCustomer().getId().equals(customerId)) {
      throw new OrderAccessDeniedException(id, customerId);
    }

    return OrderData.builder()
        .orderId(id)
        .orderedProducts(Optional.ofNullable(orderedProductRepository.findAllByOrderId(id)).orElseThrow())
        .build();
  }

  /**
   * Cancels an existing order and reverts the product quantities.
   *
   * @param id         The ID of the order to cancel.
   * @param customerId The ID of the customer associated with the order.
   * @throws OrderNotFoundException      If the order does not exist.
   * @throws CustomerNotFoundException   If the customer does not exist.
   * @throws OrderAccessDeniedException  If the customer does not own the order.
   * @throws IllegalOrderAccessException If the order status is not cancellable.
   */
  @Override
  public void delete(final UUID id, final Long customerId) {
    Assert.notNull(id, "Order ID must not be null");
    Assert.notNull(customerId, "Customer ID must not be null");

    final OrderEntity order = orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(id));
    customerRepository.findById(customerId)
        .orElseThrow(() -> new CustomerNotFoundException(customerId));

    if (!order.getCustomer().getId().equals(customerId)) {
      throw new OrderAccessDeniedException(id, customerId);
    }

    if (order.getOrderStatus() != OrderStatus.CREATED) {
      throw new IllegalOrderAccessException(order.getOrderStatus());
    }

    order.setOrderStatus(OrderStatus.CANCELLED);

    final List<OrderedProductEntity> orderedProducts = order.getOrderedProducts();
    orderedProducts.forEach(orderedProduct -> {
      final ProductEntity product = orderedProduct.getProduct();
      final BigDecimal newAmount = product.getAmount().add(orderedProduct.getAmount());
      product.setAmount(newAmount);
      productRepository.save(product);
    });

    log.info("The order was deleted");
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
   * @return The ID of the updated order.
   * @throws OrderNotFoundException     If the order does not exist.
   * @throws CustomerNotFoundException  If the customer does not exist.
   * @throws OrderAccessDeniedException If the customer does not own the order.
   */
  @Override
  public UUID updateStatus(final UUID id, final Long customerId, final OrderStatus status) {
    Assert.notNull(id, "Order ID must not be null");
    Assert.notNull(customerId, "Customer ID must not be null");
    Assert.notNull(status, "Order status must not bu null");

    final OrderEntity order = orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(id));
    customerRepository.findById(customerId)
        .orElseThrow(() -> new CustomerNotFoundException(customerId));

    if (!order.getCustomer().getId().equals(customerId)) {
      throw new OrderAccessDeniedException(id, customerId);
    }

    order.setOrderStatus(status);
    orderRepository.save(order);
    log.info("Order status was updated");

    return order.getId();
  }

  /**
   * Maps a list of product order DTOs to a list of {@link OrderedProductEntity}.
   *
   * @param orderedProductObjects List of product order DTOs.
   * @return List of {@link OrderedProductEntity}.
   */
  private List<OrderedProductEntity> mapToOrderedProducts(final List<ImmutableOrderedProductObject> orderedProductObjects) {
    return orderedProductObjects.stream()
        .map(orderedProductObject -> {
          UUID productId = orderedProductObject.getId();

          final ProductEntity product = productRepository.findById(productId)
              .orElseThrow(() -> new ProductNotFoundException(productId));

          if (!product.getIsAvailable()) {
            throw new UnavailableProductException(productId);
          }

          final BigDecimal amountAfterOrder = product.getAmount().subtract(orderedProductObject.getAmount());
          if (amountAfterOrder.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientProductException(productId);
          }

          product.setAmount(amountAfterOrder);
          productRepository.save(product);

          return OrderedProductEntity.builder()
              .product(product)
              .price(product.getPrice())
              .amount(orderedProductObject.getAmount())
              .build();
        })
        .collect(Collectors.toList());
  }

  /**
   * Filters and returns the list of ordered products that don't already exist in the order.
   *
   * @param orderedProducts List of all ordered products.
   * @param order           The existing order entity.
   * @return List of newly ordered products.
   */
  private List<OrderedProductEntity> filterUpdatedOrderedProductsFromExisting(final List<OrderedProductEntity> orderedProducts,
                                                                              final OrderEntity order) {
    return orderedProducts.stream().filter(orderedProduct -> {
          final OrderedProductEntityKey orderedProductKey = new OrderedProductEntityKey(
              orderedProduct.getProduct().getId(), order.getId()
          );

          return orderedProductRepository.findById(orderedProductKey)
              .map(existing -> {
                final BigDecimal newAmount = existing.getAmount().add(orderedProduct.getAmount());
                existing.setAmount(newAmount);
                existing.setPrice(orderedProduct.getPrice());
                orderedProductRepository.save(existing);
                return false;
              }).orElse(true);
        })
        .collect(Collectors.toList());
  }
}
