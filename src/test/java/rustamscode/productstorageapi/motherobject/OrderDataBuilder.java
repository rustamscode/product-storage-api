package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.persistance.projection.OrderedProductProjection;
import rustamscode.productstorageapi.service.dto.OrderData;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
public class OrderDataBuilder {
  public static final UUID DEFAULT_ID = null;
  public static final List<OrderedProductProjection> DEFAULT_PRODUCTS = Arrays.asList(
      ObjectMother.orderedProductDataObject().build(),
      ObjectMother.orderedProductDataObject().build()
  );

  private UUID id = DEFAULT_ID;
  private List<OrderedProductProjection> products = DEFAULT_PRODUCTS;

  public OrderDataBuilder() {
  }

  protected static OrderDataBuilder getInstance() {
    return new OrderDataBuilder();
  }

  public OrderDataBuilder withId(final UUID id) {
    this.id = id;
    return this;
  }

  public OrderDataBuilder withProducts(final List<OrderedProductProjection> products) {
    this.products = products;
    return this;
  }

  public OrderData build() {
    return OrderData.builder()
        .orderId(id)
        .orderedProducts(products)
        .build();
  }
}
