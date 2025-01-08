package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.service.dto.ImmutableProductOrderDetails;
import rustamscode.productstorageapi.service.dto.OrderedProductInfo;

import java.util.Arrays;
import java.util.List;

@Getter
public class ImmutableProductOrderDetailsBuilder {
  public static final String DEFAULT_DELIVERY_ADDRESS = "1234 Avenue";
  public static final List<OrderedProductInfo> DEFAULT_PRODUCTS = Arrays.asList
      (
          ObjectMother.immutableOrderedProductObject().build(),
          ObjectMother.immutableOrderedProductObject().build()
          );

  private String deliveryAddress = DEFAULT_DELIVERY_ADDRESS;
  private List<OrderedProductInfo> products = DEFAULT_PRODUCTS;

  private ImmutableProductOrderDetailsBuilder() {
  }

  protected static ImmutableProductOrderDetailsBuilder getInstance() {
    return new ImmutableProductOrderDetailsBuilder();
  }

  public ImmutableProductOrderDetailsBuilder withDeliveryAddress(final String deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
    return this;
  }

  public ImmutableProductOrderDetailsBuilder withProducts(final List<OrderedProductInfo> products) {
    this.products = products;
    return this;
  }

  public ImmutableProductOrderDetails build() {
    return ImmutableProductOrderDetails.builder()
        .deliveryAddress(deliveryAddress)
        .products(products)
        .build();
  }
}
