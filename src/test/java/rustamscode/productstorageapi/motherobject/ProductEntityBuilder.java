package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.persistance.entity.ProductEntity;
import rustamscode.productstorageapi.persistance.enumeration.Category;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ProductEntityBuilder {
    public static final UUID DEFAULT_ID = null;
    public static final String DEFAULT_NAME = "Product Test";
    public static final BigInteger DEFAULT_PRODUCT_NUMBER = BigInteger.valueOf(1234);
    public static final String DEFAULT_INFO = "Product Test Info";
    public static final Category DEFAULT_CATEGORY = Category.BOOKS;
    public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(1234);
    public static final BigDecimal DEFAULT_AMOUNT = BigDecimal.valueOf(1234);
    public static final LocalDateTime DEFAULT_LAST_AMOUNT_UPDATE = null;
    public static final LocalDateTime DEFAULT_CREATION_TIME = null;
    public static final Integer DEFAULT_VERSION = null;

    private UUID id = DEFAULT_ID;
    private String name = DEFAULT_NAME;
    private BigInteger productNumber = DEFAULT_PRODUCT_NUMBER;
    private String info = DEFAULT_INFO;
    private Category category = DEFAULT_CATEGORY;
    private BigDecimal price = DEFAULT_PRICE;
    private BigDecimal amount = DEFAULT_AMOUNT;
    private LocalDateTime lastAmountUpdate = DEFAULT_LAST_AMOUNT_UPDATE;
    private LocalDateTime creationTime = DEFAULT_CREATION_TIME;
    private Integer version = DEFAULT_VERSION;

    private ProductEntityBuilder() {
    }

    public static ProductEntityBuilder productEntity() {
        return new ProductEntityBuilder();
    }

    public ProductEntityBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public ProductEntityBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductEntityBuilder withProductNumber(BigInteger productNumber) {
        this.productNumber = productNumber;
        return this;
    }

    public ProductEntityBuilder withInfo(String info) {
        this.info = info;
        return this;
    }

    public ProductEntityBuilder withCategory(Category category) {
        this.category = category;
        return this;
    }

    public ProductEntityBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductEntityBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public ProductEntityBuilder withLastAmountUpdate(LocalDateTime lastAmountUpdate) {
        this.lastAmountUpdate = lastAmountUpdate;
        return this;
    }

    public ProductEntityBuilder withCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public ProductEntityBuilder withVersion(Integer version) {
        this.version = version;
        return this;
    }

    public ProductEntity build() {
        return ProductEntity.builder()
                .id(id)
                .name(name)
                .productNumber(productNumber)
                .info(info)
                .category(category)
                .price(price)
                .amount(amount)
                .lastAmountUpdate(lastAmountUpdate)
                .creationTime(creationTime)
                .version(version)
                .build();
    }


}
