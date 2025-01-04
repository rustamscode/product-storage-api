package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.web.dto.response.OrderDataResponse;
import rustamscode.productstorageapi.web.dto.response.OrderedProductDataResponse;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
public class OrderDataResponseBuilder {
  public static final UUID DEFAULT_ID = null;
  public static final List<OrderedProductDataResponse> DEFAULT_PRODUCTS = Arrays.asList(
      ObjectMother.orderedProductDataResponse().build(),
      ObjectMother.orderedProductDataResponse().build()
  );
  public static final BigDecimal DEFAULT_TOTAL_PRICE = BigDecimal.valueOf(2468);

  private UUID id = DEFAULT_ID;
  private List<OrderedProductDataResponse> products = DEFAULT_PRODUCTS;
  private BigDecimal totalPrice = DEFAULT_TOTAL_PRICE;

  public OrderDataResponseBuilder() {
  }

  protected static OrderDataResponseBuilder getInstance() {
    return new OrderDataResponseBuilder();
  }

  public OrderDataResponseBuilder withId(final UUID id) {
    this.id = id;
    return this;
  }

  public OrderDataResponseBuilder withProducts(final List<OrderedProductDataResponse> products) {
    this.products = products;
    return this;
  }

  public OrderDataResponseBuilder withTotalPrice(final BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }

  public OrderDataResponse build() {
    return OrderDataResponse.builder()
        .orderId(id)
        .orderedProducts(products)
        .totalPrice(totalPrice)
        .build();
  }
}
