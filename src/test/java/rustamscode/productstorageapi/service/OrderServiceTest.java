package rustamscode.productstorageapi.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.convert.ConversionService;
import rustamscode.productstorageapi.enumeration.OrderStatus;
import rustamscode.productstorageapi.exception.CustomerNotFoundException;
import rustamscode.productstorageapi.motherobject.ObjectMother;
import rustamscode.productstorageapi.persistance.entity.CustomerEntity;
import rustamscode.productstorageapi.persistance.entity.OrderEntity;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderServiceTest extends ServiceTest {

  @Mock
  OrderRepository orderRepositoryMock;

  @Mock
  OrderedProductRepository orderedProductRepositoryMock;

  @Mock
  CustomerRepository customerRepositoryMock;

  @Mock
  ProductRepository productRepositoryMock;

  @Mock
  ConversionService conversionServiceMock;

  @InjectMocks
  OrderServiceImpl underTest;

  OrderEntity expectedOrder;
  UUID expectedProductId;
  UUID expectedOrderId;
  Long expectedCustomerId;

  @BeforeEach
  void setup() {
    expectedProductId = UUID.randomUUID();
    expectedOrderId = UUID.randomUUID();
    expectedCustomerId = new Long(1234);
    expectedOrder = ObjectMother.orderEntity()
        .withId(expectedProductId)
        .build();
  }

  @Override
  Object getService() {
    return underTest;
  }

  @Test
  void createShouldSaveOrderAndReturnId_WhenValidInput() {
    ImmutableProductOrderDetails orderDetails = ObjectMother.immutableProductOrderDetails()
        .withProducts(List.of(ImmutableOrderedProductObject.builder()
            .id(expectedProductId)
            .amount(BigDecimal.valueOf(2))
            .build()))
        .build();

    CustomerEntity customer = new CustomerEntity();
    customer.setId(expectedCustomerId);

    ProductEntity product = new ProductEntity();
    product.setId(expectedProductId);
    product.setAmount(BigDecimal.valueOf(10));
    product.setIsAvailable(true);

    OrderEntity order = new OrderEntity();
    order.setId(expectedOrderId);

    when(customerRepositoryMock.findById(expectedCustomerId)).thenReturn(Optional.of(customer));
    when(productRepositoryMock.findById(expectedProductId)).thenReturn(Optional.of(product));
    when(conversionServiceMock.convert(orderDetails, OrderEntity.class)).thenReturn(order);
    when(orderRepositoryMock.save(order)).thenReturn(order);

    UUID actualId = underTest.create(expectedCustomerId, orderDetails);

    assertNotNull(actualId);
    assertEquals(expectedOrderId, actualId);
    verify(orderRepositoryMock, times(2)).save(any(OrderEntity.class));
    verify(orderedProductRepositoryMock, times(1)).saveAll(anyList());
  }

  @Test
  void createShouldThrowCustomerNotFoundException_WhenCustomerNotFound() {
    ImmutableProductOrderDetails orderDetails = ObjectMother.immutableProductOrderDetails().build();

    when(customerRepositoryMock.findById(expectedCustomerId)).thenReturn(Optional.empty());

    assertThrows(CustomerNotFoundException.class, () -> underTest.create(expectedCustomerId, orderDetails));
  }

  @Test
  void findByIdShouldReturnOrderData_WhenOrderExistsAndAccessible() {
    CustomerEntity customer = ObjectMother.customerEntity()
        .withId(expectedCustomerId)
        .build();

    OrderEntity order = ObjectMother.orderEntity()
        .withId(expectedOrderId)
        .withCustomer(customer)
        .build();

    when(orderRepositoryMock.findById(expectedOrderId)).thenReturn(Optional.of(order));
    when(customerRepositoryMock.findById(expectedCustomerId)).thenReturn(Optional.of(customer));

    OrderData actual = underTest.findById(expectedOrderId, expectedCustomerId);

    assertNotNull(actual);
    assertEquals(expectedOrderId, actual.getOrderId());
  }


  @Test
  void updateStatusShouldUpdateOrderStatus_WhenOrderExistsAndAccessible() {
    OrderStatus newStatus = OrderStatus.DONE;

    CustomerEntity customer = ObjectMother.customerEntity()
        .withId(expectedCustomerId)
        .build();

    OrderEntity order = ObjectMother.orderEntity()
        .withId(expectedOrderId)
        .withCustomer(customer)
        .build();

    when(orderRepositoryMock.findById(expectedOrderId)).thenReturn(Optional.of(order));
    when(customerRepositoryMock.findById(expectedCustomerId)).thenReturn(Optional.of(customer));

    UUID actual = underTest.updateStatus(expectedOrderId, expectedCustomerId, newStatus);

    assertNotNull(actual);
    assertEquals(newStatus, order.getOrderStatus());
    verify(orderRepositoryMock, times(1)).save(order);
  }
}
