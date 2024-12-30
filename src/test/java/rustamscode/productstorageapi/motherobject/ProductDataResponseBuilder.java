package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.enumeration.Category;
import rustamscode.productstorageapi.web.dto.ProductDataResponse;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ProductDataResponseBuilder {
  public static final UUID DEFAULT_ID = null;
  public static final String DEFAULT_NAME = "Product Test";
  public static final BigInteger DEFAULT_PRODUCT_NUMBER = BigInteger.valueOf(1234);
  public static final String DEFAULT_INFO = "Product Test Info";
  public static final Category DEFAULT_CATEGORY = Category.BOOKS;
  public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(1234);
  public static final BigDecimal DEFAULT_AMOUNT = BigDecimal.valueOf(1234);
  public static final LocalDateTime DEFAULT_LAST_AMOUNT_UPDATE = null;
  public static final LocalDate DEFAULT_CREATION_TIME = null;

  private UUID id = DEFAULT_ID;
  private String name = DEFAULT_NAME;
  private BigInteger productNumber = DEFAULT_PRODUCT_NUMBER;
  private String info = DEFAULT_INFO;
  private Category category = DEFAULT_CATEGORY;
  private BigDecimal price = DEFAULT_PRICE;
  private BigDecimal amount = DEFAULT_AMOUNT;
  private LocalDateTime lastAmountUpdate = DEFAULT_LAST_AMOUNT_UPDATE;
  private LocalDate creationTime = DEFAULT_CREATION_TIME;

  private ProductDataResponseBuilder() {
  }

  protected static ProductDataResponseBuilder getInstance() {
    return new ProductDataResponseBuilder();
  }

  public ProductDataResponseBuilder withId(final UUID id) {
    this.id = id;
    return this;
  }

  public ProductDataResponseBuilder withName(final String name) {
    this.name = name;
    return this;
  }

  public ProductDataResponseBuilder withProductNumber(final BigInteger productNumber) {
    this.productNumber = productNumber;
    return this;
  }

  public ProductDataResponseBuilder withInfo(final String info) {
    this.info = info;
    return this;
  }

  public ProductDataResponseBuilder withCategory(final Category category) {
    this.category = category;
    return this;
  }

  public ProductDataResponseBuilder withPrice(final BigDecimal price) {
    this.price = price;
    return this;
  }

  public ProductDataResponseBuilder withAmount(final BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public ProductDataResponseBuilder withLastAmountUpdate(final LocalDateTime lastAmountUpdate) {
    this.lastAmountUpdate = lastAmountUpdate;
    return this;
  }

  public ProductDataResponseBuilder withCreationTime(final LocalDate creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  public ProductDataResponse build() {
    return ProductDataResponse.builder()
            .id(id)
            .name(name)
            .productNumber(productNumber)
            .info(info)
            .category(category)
            .price(price)
            .amount(amount)
            .lastAmountUpdate(lastAmountUpdate)
            .creationTime(creationTime)
            .build();
  }

}
