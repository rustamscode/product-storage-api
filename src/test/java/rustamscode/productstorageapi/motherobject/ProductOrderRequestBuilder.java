package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.web.dto.OrderedProductRequest;
import rustamscode.productstorageapi.web.dto.ProductOrderRequest;

import java.util.Arrays;
import java.util.List;

@Getter
public class ProductOrderRequestBuilder {
  public static final String DEFAULT_DELIVERY_ADDRESS = "1234 Avenue";
  public static final List<OrderedProductRequest> DEFAULT_PRODUCTS = Arrays.asList
      (
          ObjectMother.orderedProductRequest().build(),
          ObjectMother.orderedProductRequest().build()
      );

  private String deliveryAddress = DEFAULT_DELIVERY_ADDRESS;
  private List<OrderedProductRequest> products = DEFAULT_PRODUCTS;

  private ProductOrderRequestBuilder() {
  }

  protected static ProductOrderRequestBuilder getInstance() {
    return new ProductOrderRequestBuilder();
  }

  public ProductOrderRequestBuilder withDeliveryAddress(final String deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
    return this;
  }

  public ProductOrderRequestBuilder withProducts(final List<OrderedProductRequest> products) {
    this.products = products;
    return this;
  }

  public ProductOrderRequest build() {
    return ProductOrderRequest.builder()
        .deliveryAddress(deliveryAddress)
        .products(products)
        .build();
  }
}
