package rustamscode.productstorageapi.web.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import rustamscode.productstorageapi.advice.GlobalExceptionHandler;
import rustamscode.productstorageapi.enumeration.Currency;
import rustamscode.productstorageapi.exception.ProductNotFoundException;
import rustamscode.productstorageapi.motherobject.ObjectMother;
import rustamscode.productstorageapi.service.ProductService;
import rustamscode.productstorageapi.service.dto.ErrorDetails;
import rustamscode.productstorageapi.service.dto.ImmutableProductCreateDetails;
import rustamscode.productstorageapi.service.dto.ImmutableProductUpdateDetails;
import rustamscode.productstorageapi.service.dto.ProductData;
import rustamscode.productstorageapi.web.dto.ProductUpdateRequest;
import rustamscode.productstorageapi.web.dto.ProductCreateRequest;
import rustamscode.productstorageapi.web.dto.ProductDataResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FieldDefaults(level = AccessLevel.PRIVATE)
class ProductControllerImplTest extends ControllerTest {

  @InjectMocks
  ProductControllerImpl underTest;

  @Mock
  ProductService productServiceMock;

  @Mock
  ConversionService conversionServiceMock;

  static final String BASE_URL = "/products";

  final UUID expectedId = UUID.randomUUID();

  @Override
  protected Object getController() {
    return underTest;
  }

  @Override
  protected HandlerMethodArgumentResolver getArgumentResolver() {
    return new PageableHandlerMethodArgumentResolver();
  }

  @Override
  protected Object getExceptionHandler() {
    return new GlobalExceptionHandler();
  }

  @Test
  void createShouldReturnCreatedProductIdWhenRequestIsValid() throws Exception {
    final ImmutableProductCreateDetails immutableCreateDetails = ObjectMother.immutableProductCreateDetails().build();
    final ProductCreateRequest createRequest = ObjectMother.productCreateRequest().build();
    when(conversionServiceMock.convert(any(ProductCreateRequest.class), eq(ImmutableProductCreateDetails.class)))
        .thenReturn(immutableCreateDetails);
    when(productServiceMock.create(immutableCreateDetails)).thenReturn(expectedId);

    final String result = mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();
    final UUID actual = objectMapper.readValue(result, UUID.class);

    assertEquals(expectedId, actual);
    verify(conversionServiceMock).convert(any(ProductCreateRequest.class), eq(ImmutableProductCreateDetails.class));
    verify(productServiceMock).create(immutableCreateDetails);
  }

  @Test
  void createShouldReturnErrorDetailsWhenRequestIsInvalidByOneField() throws Exception {
    final ProductCreateRequest invalidCreateRequest = ObjectMother.productCreateRequest()
        .withPrice(BigDecimal.valueOf(-1234))
        .build();

    final String result = mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidCreateRequest)))
        .andExpect(status().isBadRequest())
        .andReturn().getResponse().getContentAsString();
    final ErrorDetails actual = objectMapper.readValue(result, ErrorDetails.class);
    final String actualMessage = actual.getMessage();

    assertEquals("Price must be 0 or greater!", actualMessage);
  }

  @Test
  void createShouldReturnErrorDetailsWhenRequestIsInvalidBySeveralFields() throws Exception {
    final ProductCreateRequest invalidCreateRequest = ObjectMother.productCreateRequest()
        .withName("Product".repeat(40))
        .withPrice(BigDecimal.valueOf(-1234))
        .build();

    final String result = mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidCreateRequest)))
        .andExpect(status().isBadRequest())
        .andReturn().getResponse().getContentAsString();
    final ErrorDetails actual = objectMapper.readValue(result, ErrorDetails.class);
    final String actualMessage = actual.getMessage();

    assertThat(actualMessage).contains("Name must not be blank or exceed 255 characters!");
    assertThat(actualMessage).contains("Price must be 0 or greater!");
  }

  @Test
  void findByIdShouldReturnProductDetailsWhenProductExists() throws Exception {
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
  void findByIdShouldReturnErrorDetailsWhenProductNotExists() throws Exception {
    when(productServiceMock.findById(Currency.RUB, expectedId)).thenThrow(new ProductNotFoundException(expectedId));

    final String result = mockMvc.perform(get(BASE_URL + "/{id}", expectedId)
            .header("currency", "RUB"))
        .andExpect(status().isNotFound())
        .andReturn().getResponse().getContentAsString();
    final ErrorDetails actual = objectMapper.readValue(result, ErrorDetails.class);
    final String actualMessage = actual.getMessage();

    assertEquals("The product with ID " + expectedId + " was not found.", actualMessage);
  }

  @Test
  void updateShouldReturnUpdatedProductIdWhenRequestIsValid() throws Exception {
    final ProductUpdateRequest updateRequest = ObjectMother.productUpdateRequest().build();
    final ImmutableProductUpdateDetails immutableUpdateDetails = ObjectMother.immutableProductUpdateDetails().build();
    when(conversionServiceMock.convert(any(ProductUpdateRequest.class), eq(ImmutableProductUpdateDetails.class)))
        .thenReturn(immutableUpdateDetails);
    when(productServiceMock.update(expectedId, immutableUpdateDetails)).thenReturn(expectedId);

    final String result = mockMvc.perform(put(BASE_URL + "/{id}", expectedId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    final UUID actual = objectMapper.readValue(result, UUID.class);

    assertEquals(expectedId, actual);
    verify(conversionServiceMock).convert(any(ProductUpdateRequest.class), eq(ImmutableProductUpdateDetails.class));
    verify(productServiceMock).update(expectedId, immutableUpdateDetails);
  }

  @Test
  void updateShouldReturnErrorDetailsWhenRequestIsNotValid() throws Exception {
    final ProductUpdateRequest updateRequest = ObjectMother.productUpdateRequest()
        .withAmount(BigDecimal.valueOf(-1234))
        .build();

    final String result = mockMvc.perform(put(BASE_URL + "/{id}", expectedId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isBadRequest())
        .andReturn().getResponse().getContentAsString();
    final ErrorDetails actual = objectMapper.readValue(result, ErrorDetails.class);
    final String actualMessage = actual.getMessage();

    assertEquals("Amount must be 0 or greater!", actualMessage);
  }

  @Test
  void findAllShouldReturnPagedProductsWhenProductsExist() throws Exception {
    final UUID expectedId1 = UUID.randomUUID();
    final UUID expectedId2 = UUID.randomUUID();
    final ProductData data1 = ObjectMother.productData()
        .withId(expectedId1)
        .build();
    final ProductData data2 = ObjectMother.productData()
        .withId(expectedId2)
        .build();
    final ProductDataResponse dataResponse1 = ObjectMother.productDataResponse()
        .withId(expectedId1)
        .build();
    final ProductDataResponse dataResponse2 = ObjectMother.productDataResponse()
        .withId(expectedId1)
        .build();
    final Pageable pageable = PageRequest.of(0, 2);
    final Page<ProductData> productPage = new PageImpl<>(List.of(data1, data2), pageable, 2);
    when(productServiceMock.findAll(any(), any(Pageable.class))).thenReturn(productPage);
    when(conversionServiceMock.convert(data1, ProductDataResponse.class)).thenReturn(dataResponse1);
    when(conversionServiceMock.convert(data2, ProductDataResponse.class)).thenReturn(dataResponse2);

    mockMvc.perform(get(BASE_URL)
            .header("currency", "RUB")
            .param("page", "0")
            .param("size", "2")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value(dataResponse1.getId().toString()))
        .andExpect(jsonPath("$.content[0].name").value(dataResponse1.getName()))
        .andExpect(jsonPath("$.content[1].id").value(dataResponse2.getId().toString()))
        .andExpect(jsonPath("$.content[1].name").value(dataResponse2.getName()));

    verify(productServiceMock).findAll(any(), any(Pageable.class));
    verify(conversionServiceMock).convert(data1, ProductDataResponse.class);
    verify(conversionServiceMock).convert(data2, ProductDataResponse.class);
  }

  @Test
  void deleteShouldReturnNoContentWhenProductDeleted() throws Exception {
    mockMvc.perform(delete(BASE_URL + "/{id}", expectedId))
        .andExpect(status().isNoContent());

    verify(productServiceMock).delete(expectedId);
  }

  @Test
  void deleteShouldReturnErrorDetailsWhenProductNotExists() throws Exception {
    doThrow(new ProductNotFoundException(expectedId)).when(productServiceMock).delete(expectedId);

    final String result = mockMvc.perform(delete(BASE_URL + "/{id}", expectedId))
        .andExpect(status().isNotFound())
        .andReturn().getResponse().getContentAsString();
    final ErrorDetails actual = objectMapper.readValue(result, ErrorDetails.class);
    final String actualMessage = actual.getMessage();

    assertEquals("The product with ID " + expectedId + " was not found.", actualMessage);
  }
}