package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.persistance.projection.OrderedProductProjection;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class OrderedProductDataObjectBuilder {
  public static final UUID DEFAULT_ID = null;
  public static final String DEFAULT_NAME = "Product Test";
  public static final BigDecimal DEFAULT_AMOUNT = BigDecimal.valueOf(1234);
  public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(1234);

  private UUID id = DEFAULT_ID;
  private String name = DEFAULT_NAME;
  private BigDecimal amount = DEFAULT_AMOUNT;
  private BigDecimal price = DEFAULT_PRICE;

  private OrderedProductDataObjectBuilder() {
  }

  protected static OrderedProductDataObjectBuilder getInstance() {
    return new OrderedProductDataObjectBuilder();
  }

  public OrderedProductDataObjectBuilder withId(final UUID id) {
    this.id = id;
    return this;
  }

  public OrderedProductDataObjectBuilder withName(final String name) {
    this.name = name;
    return this;
  }

  public OrderedProductDataObjectBuilder withPrice(final BigDecimal price) {
    this.price = price;
    return this;
  }

  public OrderedProductDataObjectBuilder withAmount(final BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public OrderedProductProjection build() {
    return OrderedProductProjection.builder()
        .productId(id)
        .name(name)
        .price(price)
        .amount(amount)
        .build();
  }
}
