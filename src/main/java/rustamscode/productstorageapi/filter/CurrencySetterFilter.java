package rustamscode.productstorageapi.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rustamscode.productstorageapi.provider.CurrencyProvider;
import rustamscode.productstorageapi.enumeration.Currency;

import java.io.IOException;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CurrencySetterFilter extends OncePerRequestFilter {
  final CurrencyProvider currencyProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    Currency currency;
    String currencyHeader = request.getHeader("currency");

    if (currencyHeader == null) {
      filterChain.doFilter(request, response);
    }

    try {
      currency = Currency.fromCurrencyName(currencyHeader);
    } catch (Exception e) {
      log.error("Currency {} is not acceptable", currencyHeader);
      filterChain.doFilter(request, response);
      return;
    }

    currencyProvider.setCurrency(currency);
    filterChain.doFilter(request, response);
  }
}
