package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.enumeration.Category;
import rustamscode.productstorageapi.web.dto.ProductCreateRequest;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
public class ProductCreateRequestBuilder {
  public static final String DEFAULT_NAME = "Product Test";
  public static final BigInteger DEFAULT_PRODUCT_NUMBER = BigInteger.valueOf(1234);
  public static final String DEFAULT_INFO = "Product Test Info";
  public static final Category DEFAULT_CATEGORY = Category.BOOKS;
  public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(1234);
  public static final BigDecimal DEFAULT_AMOUNT = BigDecimal.valueOf(1234);

  private String name = DEFAULT_NAME;
  private BigInteger productNumber = DEFAULT_PRODUCT_NUMBER;
  private String info = DEFAULT_INFO;
  private Category category = DEFAULT_CATEGORY;
  private BigDecimal price = DEFAULT_PRICE;
  private BigDecimal amount = DEFAULT_AMOUNT;

  private ProductCreateRequestBuilder() {
  }

  protected static ProductCreateRequestBuilder getInstance() {
    return new ProductCreateRequestBuilder();
  }

  public ProductCreateRequestBuilder withName(final String name) {
    this.name = name;
    return this;
  }

  public ProductCreateRequestBuilder withProductNumber(final BigInteger productNumber) {
    this.productNumber = productNumber;
    return this;
  }

  public ProductCreateRequestBuilder withInfo(final String info) {
    this.productNumber = productNumber;
    return this;
  }

  public ProductCreateRequestBuilder withCategory(final Category category) {
    this.category = category;
    return this;
  }

  public ProductCreateRequestBuilder withPrice(final BigDecimal price) {
    this.price = price;
    return this;
  }

  public ProductCreateRequestBuilder withAmount(final BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public ProductCreateRequest build() {
    return ProductCreateRequest.builder()
            .name(name)
            .productNumber(productNumber)
            .info(info)
            .category(category)
            .price(price)
            .amount(amount)
            .build();
  }
}
