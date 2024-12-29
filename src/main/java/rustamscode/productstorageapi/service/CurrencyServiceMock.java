package rustamscode.productstorageapi.service;

import rustamscode.productstorageapi.service.dto.CurrencyRateDetails;

import java.math.BigDecimal;

public class CurrencyServiceMock implements CurrencyService {
  @Override
  public CurrencyRateDetails getCurrencyRateDetails() {
    return CurrencyRateDetails.builder()
        .RUB(BigDecimal.TWO)
        .USD(BigDecimal.TEN)
        .CNY(BigDecimal.ONE)
        .build();
  }
}
