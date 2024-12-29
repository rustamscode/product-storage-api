package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.service.dto.ImmutableProductUpdateDetails;
import rustamscode.productstorageapi.enumeration.Category;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
public class ImmutableProductUpdateDetailsBuilder {
  public static final BigInteger DEFAULT_PRODUCT_NUMBER = BigInteger.valueOf(1234);
  public static final String DEFAULT_INFO = "Product Test Info";
  public static final Category DEFAULT_CATEGORY = Category.BOOKS;
  public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(1234);
  public static final BigDecimal DEFAULT_AMOUNT = BigDecimal.valueOf(1234);

  private BigInteger productNumber = DEFAULT_PRODUCT_NUMBER;
  private String info = DEFAULT_INFO;
  private Category category = DEFAULT_CATEGORY;
  private BigDecimal price = DEFAULT_PRICE;
  private BigDecimal amount = DEFAULT_AMOUNT;

  private ImmutableProductUpdateDetailsBuilder() {
  }

  protected static ImmutableProductUpdateDetailsBuilder getInstance() {
    return new ImmutableProductUpdateDetailsBuilder();
  }

  public ImmutableProductUpdateDetailsBuilder withProductNumber(final BigInteger productNumber) {
    this.productNumber = productNumber;
    return this;
  }

  public ImmutableProductUpdateDetailsBuilder withInfo(final String info) {
    this.info = info;
    return this;
  }

  public ImmutableProductUpdateDetailsBuilder withCategory(final Category category) {
    this.category = category;
    return this;
  }

  public ImmutableProductUpdateDetailsBuilder withPrice(final BigDecimal price) {
    this.price = price;
    return this;
  }

  public ImmutableProductUpdateDetailsBuilder withAmount(final BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public ImmutableProductUpdateDetails build() {
    return ImmutableProductUpdateDetails.builder()
            .productNumber(productNumber)
            .info(info)
            .category(category)
            .price(price)
            .amount(amount)
            .build();
  }
}
