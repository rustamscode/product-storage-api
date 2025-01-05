package rustamscode.productstorageapi.advice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import rustamscode.productstorageapi.enumeration.Currency;
import rustamscode.productstorageapi.provider.CurrencyProvider;
import rustamscode.productstorageapi.provider.CurrencyRateProvider;
import rustamscode.productstorageapi.web.controller.ProductControllerImpl;
import rustamscode.productstorageapi.web.dto.response.ProductDataResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@RestControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyConverterAdvice implements ResponseBodyAdvice<Object> {

  final CurrencyProvider currencyProvider;
  final CurrencyRateProvider currencyRateProvider;

  final static Set<String> SUPPORTED_METHODS = Set.of("findById", "findAll", "search");

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    String methodName = returnType.getMethod().getName();
    Class<?> declaringClass = returnType.getMethod().getDeclaringClass();

    return SUPPORTED_METHODS.stream().anyMatch(methodName::contains)
           && declaringClass == ProductControllerImpl.class;
  }

  @Override
  public Object beforeBodyWrite(@Nullable Object body,
                                MethodParameter returnType,
                                MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request, ServerHttpResponse response) {
    if (body == null) return null;

    final Currency currency = currencyProvider.getCurrency();
    final BigDecimal currencyRate = currencyRateProvider.getCurrencyRate(currency);

    if (body instanceof ProductDataResponse responseBody) {
      return processResponse(responseBody, currency, currencyRate);
    } else if (body instanceof Page<?> responseBody) {
      return processResponsePage((Page<ProductDataResponse>) responseBody, currency, currencyRate);
    }

    return body;
  }

  private ProductDataResponse processResponse(ProductDataResponse body, Currency currency, BigDecimal currencyRate) {
    BigDecimal newPrice = body.getPrice().divide(currencyRate, RoundingMode.HALF_UP);
    body.setPrice(newPrice);
    body.setCurrency(currency);

    return body;
  }

  private Page<ProductDataResponse> processResponsePage(Page<ProductDataResponse> page, Currency currency, BigDecimal currencyRate) {
    return page.map(response -> processResponse(response, currency, currencyRate));
  }
}