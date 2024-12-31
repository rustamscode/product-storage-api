package rustamscode.productstorageapi.motherobject;

import lombok.Getter;
import rustamscode.productstorageapi.interaction.dto.CurrencyRateDetails;

import java.math.BigDecimal;

@Getter
public class CurrencyRateDetailsBuilder {
  public static final BigDecimal DEFAULT_RUB = BigDecimal.ONE;
  public static final BigDecimal DEFAULT_USD = BigDecimal.TEN;
  public static final BigDecimal DEFAULT_CNY = BigDecimal.TWO;

  private BigDecimal RUB = DEFAULT_RUB;
  private BigDecimal USD = DEFAULT_USD;
  private BigDecimal CNY = DEFAULT_CNY;

  private CurrencyRateDetailsBuilder() {
  }

  protected static CurrencyRateDetailsBuilder getInstance() {
    return new CurrencyRateDetailsBuilder();
  }

  public CurrencyRateDetailsBuilder withRub(final BigDecimal RUB) {
    this.RUB = RUB;
    return this;
  }

  public CurrencyRateDetailsBuilder withUsd(final BigDecimal USD) {
    this.USD = USD;
    return this;
  }

  public CurrencyRateDetailsBuilder withCny(final BigDecimal CNY) {
    this.CNY = CNY;
    return this;
  }

  public CurrencyRateDetails build() {
    return CurrencyRateDetails.builder()
        .RUB(RUB)
        .USD(USD)
        .CNY(CNY)
        .build();
  }
}
