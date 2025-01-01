package rustamscode.productstorageapi.advice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import rustamscode.productstorageapi.web.dto.response.ProductDataResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyConverterAdvice implements ResponseBodyAdvice<Object> {

  final CurrencyProvider currencyProvider;
  final CurrencyRateProvider currencyRateProvider;

  Currency currency;
  BigDecimal currencyRate;

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    String methodName = returnType.getMethod().getName();

    return methodName.contains("findById") || methodName.contains("findAll") || methodName.contains("search");
  }

  @Override
  public Object beforeBodyWrite(@Nullable Object body,
                                MethodParameter returnType,
                                MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request, ServerHttpResponse response) {
    this.currency = currencyProvider.getCurrency();
    this.currencyRate = currencyRateProvider.getCurrencyRate(currencyProvider.getCurrency());

    if (body instanceof ProductDataResponse responseBody) {
      return processResponse(responseBody);
    } else if (body instanceof Page<?> responseBody) {
      return processResponsePage((Page<ProductDataResponse>) responseBody);
    }

    return body;
  }

  private ProductDataResponse processResponse(ProductDataResponse body) {
    BigDecimal newPrice = body.getPrice().divide(currencyRate, RoundingMode.HALF_UP);
    body.setPrice(newPrice);
    body.setCurrency(currency);

    return body;
  }

  private Page<ProductDataResponse> processResponsePage(Page<ProductDataResponse> page) {
    List<ProductDataResponse> updatedContent = page.getContent()
        .stream()
        .map(this::processResponse)
        .toList();

    return new PageImpl<>(updatedContent, page.getPageable(), page.getSize());
  }
}

