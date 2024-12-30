package rustamscode.productstorageapi.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import rustamscode.productstorageapi.exception.CustomerNotFoundException;
import rustamscode.productstorageapi.exception.OutOfStockException;
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

import java.math.BigDecimal;
import java.util.List;
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
    Assert.notNull(immutableProductOrderDetails, "Order details must not be null");

    List<ImmutableOrderedProductObject> orderedProductObjects = immutableProductOrderDetails.getProducts();

    CustomerEntity customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new CustomerNotFoundException(customerId));

    List<OrderedProductEntity> orderedProducts = mapToOrderedProducts(orderedProductObjects);
    OrderEntity order = conversionService.convert(immutableProductOrderDetails, OrderEntity.class);
    order.setCustomer(customer);
    OrderEntity savedOrder = orderRepository.save(order);
    log.info("Order was saved without products");

    orderedProducts.forEach(product -> product.setOrder(savedOrder));
    orderedProductRepository.saveAll(orderedProducts);
    log.info("Ordered products were saved");

    savedOrder.setOrderedProducts(orderedProducts);
    orderRepository.save(savedOrder);
    log.info("Order with products was saved");

    return savedOrder.getId();
  }


  private List<OrderedProductEntity> mapToOrderedProducts(List<ImmutableOrderedProductObject> orderedProductObjects) {
    return orderedProductObjects.stream()
        .map(orderedProductObject -> {
          UUID productId = orderedProductObject.getId();

          ProductEntity product = productRepository.findById(productId)
              .orElseThrow(() -> new ProductNotFoundException(productId));

          BigDecimal amountAfterOrder = product.getAmount().subtract(orderedProductObject.getAmount());
          if (amountAfterOrder.compareTo(BigDecimal.ZERO) < 0) {
            throw new OutOfStockException(productId);
          }

          if (!product.isAvailable()) {
            throw new UnavailableProductException(productId);
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
