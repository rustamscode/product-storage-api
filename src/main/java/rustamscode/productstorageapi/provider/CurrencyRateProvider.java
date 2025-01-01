package rustamscode.productstorageapi.provider;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.enumeration.Currency;
import rustamscode.productstorageapi.interaction.CurrencyServiceClient;
import rustamscode.productstorageapi.interaction.dto.CurrencyRateDetails;
import rustamscode.productstorageapi.util.DefaultJsonValueLoader;

import java.math.BigDecimal;
import java.util.Optional;

@Data
@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyRateProvider {

  final CurrencyServiceClient currencyServiceClient;
  final DefaultJsonValueLoader defaultValueLoader;

  public BigDecimal getCurrencyRate(Currency currency) {
    final CurrencyRateDetails currencyRates = fetchCurrencyRateDetails();

    return switch (currency) {
      case RUB -> currencyRates.getRUB();
      case USD -> currencyRates.getUSD();
      case CNY -> currencyRates.getCNY();
    };
  }

  private CurrencyRateDetails fetchCurrencyRateDetails() {
    final Optional<CurrencyRateDetails> currencyRates = Optional.ofNullable(
        currencyServiceClient.getCurrencyRateDetails()
    );

    return currencyRates.orElse(defaultValueLoader.getCurrencyRateDetails());
  }
}
