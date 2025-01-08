package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.service.dto.OrderedProductInfo;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class ImmutableOrderedProductObjectBuilder {
  public static final UUID DEFAULT_ID = null;
  public static final BigDecimal DEFAULT_AMOUNT = BigDecimal.valueOf(1234);

  private UUID id = DEFAULT_ID;
  private BigDecimal amount = DEFAULT_AMOUNT;

  private ImmutableOrderedProductObjectBuilder() {
  }

  protected static ImmutableOrderedProductObjectBuilder getInstance() {
    return new ImmutableOrderedProductObjectBuilder();
  }

  public ImmutableOrderedProductObjectBuilder withId(final UUID id) {
    this.id = id;
    return this;
  }

  public ImmutableOrderedProductObjectBuilder withAmount(final BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public OrderedProductInfo build() {
    return OrderedProductInfo.builder()
        .id(id)
        .amount(amount)
        .build();
  }
}

