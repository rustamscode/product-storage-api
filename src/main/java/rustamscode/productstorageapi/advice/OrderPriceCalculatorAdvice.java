package rustamscode.productstorageapi.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import rustamscode.productstorageapi.web.controller.OrderControllerImpl;
import rustamscode.productstorageapi.web.dto.OrderDataResponse;
import rustamscode.productstorageapi.web.dto.OrderedProductDataResponse;

import java.math.BigDecimal;

@Slf4j
@RestControllerAdvice
public class OrderPriceCalculatorAdvice implements ResponseBodyAdvice<OrderDataResponse> {

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    final String methodName = returnType.getMethod().getName();
    final Class<?> declaringClass = returnType.getMethod().getDeclaringClass();

    return methodName.contains("findById") && declaringClass == OrderControllerImpl.class;
  }

  @Override
  public OrderDataResponse beforeBodyWrite(@Nullable OrderDataResponse body,
                                           MethodParameter returnType,
                                           MediaType selectedContentType,
                                           Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                           ServerHttpRequest request,
                                           ServerHttpResponse response) {

    if (body instanceof OrderDataResponse) {
      body.setTotalPrice(calculateOrderPrice(body));
    }
    return body;
  }

  private BigDecimal calculateOrderPrice(final OrderDataResponse responseBody) {
    return responseBody.getOrderedProducts()
        .stream()
        .map(OrderedProductDataResponse::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
