package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.persistance.enumeration.Category;
import rustamscode.productstorageapi.service.dto.ImmutableProductCreateDetails;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
public class ImmutableProductCreateDetailsBuilder {
  public static final String DEFAULT_NAME = "Product Test";
  public static final BigInteger DEFAULT_PRODUCT_NUMBER = BigInteger.valueOf(1234);
  public static final String DEFAULT_INFO = "Product Test Info";
  public static final Category DEFAULT_CATEGORY = Category.BOOKS;
  public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(1234);
  public static final BigDecimal DEFAULT_AMOUNT = BigDecimal.valueOf(1234);
  public static final LocalDateTime DEFAULT_LAST_AMOUNT_UPDATE = null;

  private String name = DEFAULT_NAME;
  private BigInteger productNumber = DEFAULT_PRODUCT_NUMBER;
  private String info = DEFAULT_INFO;
  private Category category = DEFAULT_CATEGORY;
  private BigDecimal price = DEFAULT_PRICE;
  private BigDecimal amount = DEFAULT_AMOUNT;
  private LocalDateTime lastAmountUpdate = DEFAULT_LAST_AMOUNT_UPDATE;

  private ImmutableProductCreateDetailsBuilder() {
  }

  protected static ImmutableProductCreateDetailsBuilder getInstance() {
    return new ImmutableProductCreateDetailsBuilder();
  }

  public ImmutableProductCreateDetailsBuilder withName(final String name) {
    this.name = name;
    return this;
  }

  public ImmutableProductCreateDetailsBuilder withProductNumber(final BigInteger productNumber) {
    this.productNumber = productNumber;
    return this;
  }

  public ImmutableProductCreateDetailsBuilder withInfo(final String info) {
    this.productNumber = productNumber;
    return this;
  }

  public ImmutableProductCreateDetailsBuilder withCategory(final Category category) {
    this.category = category;
    return this;
  }

  public ImmutableProductCreateDetailsBuilder withPrice(final BigDecimal price) {
    this.price = price;
    return this;
  }

  public ImmutableProductCreateDetailsBuilder withAmount(final BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public ImmutableProductCreateDetailsBuilder withLastAmountUpdate(final LocalDateTime lastAmountUpdate) {
    this.lastAmountUpdate = lastAmountUpdate;
    return this;
  }

  public ImmutableProductCreateDetails build() {
    return ImmutableProductCreateDetails.builder()
            .name(name)
            .productNumber(productNumber)
            .info(info)
            .category(category)
            .price(price)
            .amount(amount)
            .lastAmountUpdate(lastAmountUpdate)
            .build();
  }
}
