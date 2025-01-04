package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.web.dto.response.OrderedProductDataResponse;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class OrderedProductDataResponseBuilder {
  public static final UUID DEFAULT_ID = null;
  public static final String DEFAULT_NAME = "Product Test";
  public static final BigDecimal DEFAULT_AMOUNT = BigDecimal.valueOf(1234);
  public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(1234);

  private UUID id = DEFAULT_ID;
  private String name = DEFAULT_NAME;
  private BigDecimal amount = DEFAULT_AMOUNT;
  private BigDecimal price = DEFAULT_PRICE;

  private OrderedProductDataResponseBuilder() {
  }

  protected static OrderedProductDataResponseBuilder getInstance() {
    return new OrderedProductDataResponseBuilder();
  }

  public OrderedProductDataResponseBuilder withId(final UUID id) {
    this.id = id;
    return this;
  }

  public OrderedProductDataResponseBuilder withName(final String name) {
    this.name = name;
    return this;
  }

  public OrderedProductDataResponseBuilder withPrice(final BigDecimal price) {
    this.price = price;
    return this;
  }

  public OrderedProductDataResponseBuilder withAmount(final BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public OrderedProductDataResponse build() {
    return OrderedProductDataResponse.builder()
        .productId(id)
        .name(name)
        .price(price)
        .amount(amount)
        .build();
  }
}
