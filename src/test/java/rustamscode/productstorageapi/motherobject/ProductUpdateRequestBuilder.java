package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.enumeration.Category;
import rustamscode.productstorageapi.web.dto.request.ProductUpdateRequest;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
public class ProductUpdateRequestBuilder {
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

  private ProductUpdateRequestBuilder() {
  }

  protected static ProductUpdateRequestBuilder getInstance() {
    return new ProductUpdateRequestBuilder();
  }

  public ProductUpdateRequestBuilder withProductNumber(final BigInteger productNumber) {
    this.productNumber = productNumber;
    return this;
  }

  public ProductUpdateRequestBuilder withInfo(final String info) {
    this.info = info;
    return this;
  }

  public ProductUpdateRequestBuilder withCategory(final Category category) {
    this.category = category;
    return this;
  }

  public ProductUpdateRequestBuilder withPrice(final BigDecimal price) {
    this.price = price;
    return this;
  }

  public ProductUpdateRequestBuilder withAmount(final BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public ProductUpdateRequest build() {
    return ProductUpdateRequest.builder()
            .productNumber(productNumber)
            .info(info)
            .category(category)
            .price(price)
            .amount(amount)
            .build();
  }
}
