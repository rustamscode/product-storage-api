package rustamscode.productstorageapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import rustamscode.productstorageapi.service.dto.OrderData;
import rustamscode.productstorageapi.service.dto.OrderStatusChangeInfo;
import rustamscode.productstorageapi.service.dto.OrderedProductInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

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
   * Creates a new order for a specific customer with the specified products.
   *
   * @param customerId The ID of the customer.
   * @param address    The order address.
   * @param products   Products that were ordered.
   * @return The ID of the created order.
   * @throws CustomerNotFoundException    If the customer does not exist.
   * @throws UnavailableProductException  If any product is unavailable.
   * @throws InsufficientProductException If the product stock is insufficient.
   */
  @Override
  @Transactional
  public UUID create(final Long customerId,
                     final String address,
                     final List<OrderedProductInfo> products) {
    Assert.notNull(customerId, "Customer ID must not be null");
    Assert.notNull(address, "Address must not be null");
    Assert.notNull(products, "Products must not be null");

    final CustomerEntity customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new CustomerNotFoundException(customerId));

    final List<OrderedProductEntity> orderedProducts = mapToOrderedProducts(products);
    final OrderEntity order = new OrderEntity();
    order.setOrderStatus(OrderStatus.CREATED);
    order.setDeliveryAddress(address);
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
  @Transactional
  public UUID update(final UUID id,
                     final Long customerId,
                     final List<OrderedProductInfo> orderedProductObjects) {
    Assert.notNull(id, "Order ID must not be null");
    Assert.notNull(customerId, "Customer ID must not be null");
    Assert.notNull(orderedProductObjects, "Ordered product list must not be null");

    final OrderEntity order = orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(id));
    customerRepository.findById(customerId)
        .orElseThrow(() -> new CustomerNotFoundException(customerId));

    if (!Objects.equals(order.getCustomer().getId(), customerId)) {
      throw new OrderAccessDeniedException(id, customerId);
    }

    if (order.getOrderStatus() != OrderStatus.CREATED) {
      throw new IllegalOrderAccessException(order.getOrderStatus());
    }

    final List<OrderedProductEntity> orderedProducts = mapToOrderedProducts(orderedProductObjects);

    orderedProducts.forEach(orderedProduct -> {
      orderedProduct.setId(new OrderedProductEntityKey(orderedProduct.getProduct().getId(), order.getId()));
      orderedProduct.setOrder(order);
    });

    final List<OrderedProductEntityKey> orderedProductIds = orderedProducts.stream()
        .map(OrderedProductEntity::getId)
        .collect(Collectors.toList());

    final Map<OrderedProductEntityKey, OrderedProductEntity> existingProductMap = orderedProductRepository
        .findAllById(orderedProductIds).stream()
        .collect(Collectors.toMap(OrderedProductEntity::getId, identity()));

    final List<OrderedProductEntity> existingProducts = orderedProducts.stream()
        .filter(orderedProduct -> existingProductMap.containsKey(orderedProduct.getId()))
        .map(orderedProduct -> {
          final OrderedProductEntity existing = existingProductMap.get(orderedProduct.getId());
          final BigDecimal newAmount = existing.getAmount().add(orderedProduct.getAmount());
          existing.setAmount(newAmount);
          existing.setPrice(orderedProduct.getPrice());
          return existing;
        })
        .toList();

    final List<OrderedProductEntity> newProducts = orderedProducts.stream()
        .filter(orderedProduct -> !existingProductMap.containsKey(orderedProduct.getId()))
        .collect(Collectors.toList());

    order.getOrderedProducts().addAll(newProducts);
    orderedProductRepository.saveAll(existingProducts);
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
  @Transactional
  public OrderData findById(final UUID id, final Long customerId) {
    Assert.notNull(id, "Order ID must not be null");
    Assert.notNull(customerId, "Customer ID must not be null");

    final OrderEntity order = orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(id));
    customerRepository.findById(customerId)
        .orElseThrow(() -> new CustomerNotFoundException(customerId));

    if (!Objects.equals(order.getCustomer().getId(), customerId)) {
      throw new OrderAccessDeniedException(id, customerId);
    }

    BigDecimal totalPrice = order.getOrderedProducts().stream()
        .map(it -> it.getPrice().multiply(it.getAmount()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    return OrderData.builder()
        .orderId(id)
        .orderedProducts(Optional.ofNullable(orderedProductRepository.findProjectionsByOrderId(id)).orElseThrow())
        .totalPrice(totalPrice)
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
  @Transactional
  public void delete(final UUID id, final Long customerId) {
    Assert.notNull(id, "Order ID must not be null");
    Assert.notNull(customerId, "Customer ID must not be null");

    final OrderEntity order = orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(id));
    customerRepository.findById(customerId)
        .orElseThrow(() -> new CustomerNotFoundException(customerId));

    if (!Objects.equals(order.getCustomer().getId(), customerId)) {
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

    //orderRepository.save(order);

    log.info("The order was deleted");
  }

  @Override
  @Transactional
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
  @Transactional
  public UUID updateStatus(final UUID id, final Long customerId, final OrderStatusChangeInfo status) {
    Assert.notNull(id, "Order ID must not be null");
    Assert.notNull(customerId, "Customer ID must not be null");
    Assert.notNull(status, "Order status must not bu null");

    final OrderEntity order = orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(id));
    customerRepository.findById(customerId)
        .orElseThrow(() -> new CustomerNotFoundException(customerId));

    if (!Objects.equals(order.getCustomer().getId(), customerId)) {
      throw new OrderAccessDeniedException(id, customerId);
    }

    order.setOrderStatus(status.getStatus());
    orderRepository.save(order);
    log.info("Order status was updated");

    return order.getId();
  }

  /**
   * Maps a list of product order DTOs to a list of {@link OrderedProductEntity}.
   *
   * @param orderedProductInfos List of product order DTOs.
   * @return List of {@link OrderedProductEntity}.
   */
  private List<OrderedProductEntity> mapToOrderedProducts(final List<OrderedProductInfo> orderedProductInfos) {
    final List<UUID> productIds = orderedProductInfos.stream()
        .map(OrderedProductInfo::getId)
        .collect(Collectors.toList());
    final Map<UUID, ProductEntity> productMap = productRepository.findAllById(productIds).stream()
        .collect(Collectors.toMap(ProductEntity::getId, identity()));

    return orderedProductInfos.stream()
        .map(orderedProductObject -> {
          UUID productId = orderedProductObject.getId();

          final ProductEntity product = productMap.getOrDefault(productId, null);
          Optional.ofNullable(product).orElseThrow(() -> new ProductNotFoundException(productId));

          if (!product.getIsAvailable()) {
            throw new UnavailableProductException(productId);
          }

          final BigDecimal amountAfterOrder = product.getAmount().subtract(orderedProductObject.getAmount());
          if (amountAfterOrder.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientProductException(productId);
          }

          product.setAmount(amountAfterOrder);
          productRepository.save(product);

          OrderedProductEntity orderedProduct = new OrderedProductEntity();
          orderedProduct.setProduct(product);
          orderedProduct.setPrice(product.getPrice());
          orderedProduct.setAmount(orderedProductObject.getAmount());

          return orderedProduct;
        })
        .collect(Collectors.toList());
  }
}
