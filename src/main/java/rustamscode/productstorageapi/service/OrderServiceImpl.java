package rustamscode.productstorageapi.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  final OrderRepository orderRepository;

  final OrderedProductRepository orderedProductRepository;

  final CustomerRepository customerRepository;

  final ProductRepository productRepository;

  final ConversionService conversionService;

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

    orderedProducts.forEach(product -> product.setOrder(savedOrder));
    orderedProductRepository.saveAll(orderedProducts);
    log.info("Ordered products were saved");

    savedOrder.setOrderedProducts(orderedProducts);
    orderRepository.save(savedOrder);
    log.info("Order with products was saved");

    return savedOrder.getId();
  }

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

    final List<OrderedProductEntity> orderedProducts = mapToOrderedProducts(orderedProductObjects);
    orderedProducts.forEach(product -> product.setOrder(order));

    order.getOrderedProducts().addAll(orderedProducts);
    orderRepository.save(order);
    log.info("Updated order was saved");

    return id;
  }

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
      final BigDecimal newAmount = product.getAmount().add(orderedProduct.getOrderedProductAmount());
      product.setAmount(newAmount);
      productRepository.save(product);
    });

    log.info("The order was deleted");
  }

  @Override
  public void confirm(final UUID id, final Long customerId) {
    //TODO Implementation
  }

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
              .orderedProductPrice(product.getPrice())
              .orderedProductAmount(orderedProductObject.getAmount())
              .build();
        })
        .collect(Collectors.toList());
  }
}
