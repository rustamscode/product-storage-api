package rustamscode.productstorageapi.web.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rustamscode.productstorageapi.advice.GlobalExceptionHandler;
import rustamscode.productstorageapi.enumeration.Currency;
import rustamscode.productstorageapi.motherobject.ObjectMother;
import rustamscode.productstorageapi.service.ProductService;
import rustamscode.productstorageapi.service.dto.ProductData;
import rustamscode.productstorageapi.web.dto.ProductDataResponse;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductControllerIntegrationTest {

  MockMvc mockMvc;
  ObjectMapper objectMapper = new ObjectMapper()
      .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
      .registerModule(new JavaTimeModule());

  @Autowired
  ProductControllerImpl underTest;

  @MockBean
  ProductService productServiceMock;

  @MockBean
  FormattingConversionService conversionServiceMock;

  static final String BASE_URL = "/products";
  final UUID expectedId = UUID.randomUUID();

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(underTest)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  void findByIdShouldReturnProductDetailsWithRubCurrencyWhenProductExists() throws Exception {
    final ProductData data = ObjectMother.productData().build();
    final ProductDataResponse dataResponse = ObjectMother.productDataResponse().build();

    when(productServiceMock.findById(Currency.RUB, expectedId)).thenReturn(data);
    when(conversionServiceMock.convert(any(ProductData.class), eq(ProductDataResponse.class))).thenReturn(dataResponse);

    final String result = mockMvc.perform(get(BASE_URL + "/{id}", expectedId)
            .header("currency", "RUB"))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    final ProductDataResponse actual = objectMapper.readValue(result, ProductDataResponse.class);

    assertEquals(dataResponse.getId(), actual.getId());
    assertEquals(dataResponse.getName(), actual.getName());
    assertEquals(dataResponse.getCurrency(), actual.getCurrency());
    verify(productServiceMock).findById(Currency.RUB, expectedId);
    verify(conversionServiceMock).convert(any(ProductData.class), eq(ProductDataResponse.class));
  }

  @Test
  void findByIdShouldReturnProductDetailsWithUsdCurrencyWhenProductExists() throws Exception {
    final ProductData data = ObjectMother.productData().build();
    final ProductDataResponse dataResponse = ObjectMother.productDataResponse().build();

    when(productServiceMock.findById(Currency.USD, expectedId)).thenReturn(data);
    when(conversionServiceMock.convert(any(ProductData.class), eq(ProductDataResponse.class))).thenReturn(dataResponse);

    final String result = mockMvc.perform(get(BASE_URL + "/{id}", expectedId)
            .header("currency", "USD"))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    final ProductDataResponse actual = objectMapper.readValue(result, ProductDataResponse.class);

    assertEquals(dataResponse.getId(), actual.getId());
    assertEquals(dataResponse.getName(), actual.getName());
    assertEquals(dataResponse.getCurrency(), actual.getCurrency());
    verify(productServiceMock).findById(Currency.USD, expectedId);
    verify(conversionServiceMock).convert(any(ProductData.class), eq(ProductDataResponse.class));
  }
}
