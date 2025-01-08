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
import rustamscode.productstorageapi.service.dto.ImmutableProductOrderDetails;
import rustamscode.productstorageapi.service.dto.OrderData;
import rustamscode.productstorageapi.service.dto.OrderStatusChangeInfo;
import rustamscode.productstorageapi.service.dto.OrderedProductInfo;

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
  void createShouldThrowCustomerNotFoundException_WhenCustomerNotFound() {
    ImmutableProductOrderDetails orderDetails = ObjectMother.immutableProductOrderDetails().build();

    when(customerRepositoryMock.findById(expectedCustomerId)).thenReturn(Optional.empty());

    assertThrows(CustomerNotFoundException.class, () -> underTest.create(expectedCustomerId, "Address Test", List.of
        (
            OrderedProductInfo.builder().build(),
            OrderedProductInfo.builder().build()
        )
    ));
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
    OrderStatusChangeInfo newStatus = OrderStatusChangeInfo.builder()
        .status(OrderStatus.DONE)
        .build();

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
    assertEquals(newStatus.getStatus(), order.getOrderStatus());
    verify(orderRepositoryMock, times(1)).save(order);
  }
}
