package rustamscode.productstorageapi.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rustamscode.productstorageapi.currency.CurrencyProvider;
import rustamscode.productstorageapi.enumeration.Currency;

import java.io.IOException;
import java.util.Optional;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CurrencySetterFilter extends OncePerRequestFilter {
  final CurrencyProvider currencyProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    final String currency = request.getHeader("currency");
    Optional.ofNullable(currency)
        .map(Currency::valueOf)
        .ifPresent(currencyProvider::setCurrency);

    filterChain.doFilter(request, response);
  }
}
