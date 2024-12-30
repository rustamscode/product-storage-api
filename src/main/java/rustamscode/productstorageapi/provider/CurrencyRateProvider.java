package rustamscode.productstorageapi.provider;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.enumeration.Currency;
import rustamscode.productstorageapi.service.CurrencyService;
import rustamscode.productstorageapi.service.dto.CurrencyRateDetails;
import rustamscode.productstorageapi.util.DefaultValueLoader;

import java.math.BigDecimal;

@Data
@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyRateProvider {

  final CurrencyService currencyService;
  final DefaultValueLoader defaultValueLoader;

  public BigDecimal getCurrencyRate(Currency currency) {
    CurrencyRateDetails currencyRates = fetchCurrencyRateDetails();
    BigDecimal rate = null;

    switch (currency) {
      case RUB -> rate = currencyRates.getRUB();
      case USD -> rate = currencyRates.getUSD();
      case CNY -> rate = currencyRates.getCNY();
    }
    return rate;
  }

  private CurrencyRateDetails fetchCurrencyRateDetails() {
    CurrencyRateDetails currencyRates;

    try {
      currencyRates = currencyService.getCurrencyRateDetails();
    } catch (Exception e) {
      log.error("Error getting currency rates from currency service: {}", e.getMessage());
      currencyRates = defaultValueLoader.getCurrencyRateDetails();
    }
    return currencyRates;
  }
}
