package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.enumeration.OrderStatus;
import rustamscode.productstorageapi.persistance.entity.CustomerEntity;
import rustamscode.productstorageapi.persistance.entity.OrderEntity;
import rustamscode.productstorageapi.persistance.entity.OrderedProductEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
public class OrderEntityBuilder {
  public final static UUID DEFAULT_ID = null;
  public final static CustomerEntity DEFAULT_CUSTOMER = ObjectMother.customerEntity().build();
  public final static OrderStatus DEFAULT_ORDER_STATUS = OrderStatus.CREATED;
  public final static String DEFAULT_DELIVERY_ADDRESS = "Avenue 1234";
  public final static List<OrderedProductEntity> DEFAULT_ORDERED_PRODUCTS = Arrays.asList(
      OrderedProductEntity.builder()
          .order(ObjectMother.orderEntity().build())
          .orderedProductPrice(BigDecimal.valueOf(1234))
          .orderedProductAmount(BigDecimal.valueOf(1234))
          .product(ObjectMother.productEntity().build())
          .build(),
      OrderedProductEntity.builder()
          .order(ObjectMother.orderEntity().build())
          .orderedProductPrice(BigDecimal.valueOf(1234))
          .orderedProductAmount(BigDecimal.valueOf(1234))
          .product(ObjectMother.productEntity().build())
          .build()
  );

  private UUID id = DEFAULT_ID;
  private CustomerEntity customer = DEFAULT_CUSTOMER;
  private OrderStatus status = DEFAULT_ORDER_STATUS;
  private String deliveryAddress = DEFAULT_DELIVERY_ADDRESS;
  private List<OrderedProductEntity> orderedProducts = DEFAULT_ORDERED_PRODUCTS;

  private OrderEntityBuilder() {
  }

  protected static OrderEntityBuilder getInstance() {
    return new OrderEntityBuilder();
  }

  public OrderEntityBuilder withId(final UUID id) {
    this.id = id;
    return this;
  }

  public OrderEntityBuilder withCustomer(final CustomerEntity customer) {
    this.customer = customer;
    return this;
  }

  public OrderEntityBuilder withStatus(final OrderStatus status) {
    this.status = status;
    return this;
  }

  public OrderEntityBuilder withDeliveryAddress(final String deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
    return this;
  }

  public OrderEntityBuilder withOrderedProducts(final List<OrderedProductEntity> orderedProducts) {
    this.orderedProducts = orderedProducts;
    return this;
  }

  public OrderEntity build() {
    return OrderEntity.builder()
        .id(id)
        .customer(customer)
        .orderStatus(status)
        .deliveryAddress(deliveryAddress)
        .orderedProducts(orderedProducts)
        .build();
  }
}
