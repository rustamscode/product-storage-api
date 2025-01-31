package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.web.dto.OrderedProductRequest;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class OrderedProductRequestBuilder {
  public static final UUID DEFAULT_ID = null;
  public static final BigDecimal DEFAULT_AMOUNT = BigDecimal.valueOf(1234);

  private UUID id = DEFAULT_ID;
  private BigDecimal amount = DEFAULT_AMOUNT;

  private OrderedProductRequestBuilder() {
  }

  protected static OrderedProductRequestBuilder getInstance() {
    return new OrderedProductRequestBuilder();
  }

  public OrderedProductRequestBuilder withId(final UUID id) {
    this.id = id;
    return this;
  }

  public OrderedProductRequestBuilder withAmount(final BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public OrderedProductRequest build() {
    return OrderedProductRequest.builder()
        .id(id)
        .amount(amount)
        .build();
  }
}
