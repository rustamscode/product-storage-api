package rustamscode.productstorageapi.enumeration;

import rustamscode.productstorageapi.exception.UnsupportedCurrencyException;

public enum Currency {
  RUB, USD, CNY;

  public static Currency fromCurrencyName(String currencyName) {
    for (var currency : Currency.values()) {
      if (currency.name().equals(currencyName)) {
        return currency;
      }
    }
    throw new UnsupportedCurrencyException(currencyName);
  }
}
