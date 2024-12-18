package rustamscode.productstorageapi.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rustamscode.productstorageapi.config.GlobalExceptionHandler;
import rustamscode.productstorageapi.exception.ProductNotFoundException;
import rustamscode.productstorageapi.persistance.enumeration.Category;
import rustamscode.productstorageapi.service.ProductService;
import rustamscode.productstorageapi.service.dto.ImmutableProductCreateDetails;
import rustamscode.productstorageapi.service.dto.ImmutableProductUpdateDetails;
import rustamscode.productstorageapi.service.dto.ProductData;
import rustamscode.productstorageapi.web.dto.ProductCreateRequest;
import rustamscode.productstorageapi.web.dto.ProductDataResponse;
import rustamscode.productstorageapi.web.dto.ProductUpdateRequest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductControllerImpl.class)
class ProductControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private FormattingConversionService conversionService;

    private static final String BASE_URL = "/products";

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductControllerImpl(productService, conversionService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createShouldReturnCreatedProductIdWhenRequestIsValid() throws Exception {
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("Product1")
                .productNumber(BigInteger.valueOf(1111))
                .info("Test Info")
                .category(Category.BOOKS)
                .price(BigDecimal.valueOf(1111))
                .amount(BigDecimal.valueOf(1111))
                .build();
        ImmutableProductCreateDetails createDetails = ImmutableProductCreateDetails.builder()
                .name(request.getName())
                .productNumber(request.getProductNumber())
                .info(request.getInfo())
                .category(request.getCategory())
                .price(request.getPrice())
                .amount(request.getAmount())
                .lastUpdateTime(LocalDateTime.now())
                .build();
        UUID createdId = UUID.randomUUID();

        when(conversionService.convert(any(ProductCreateRequest.class), eq(ImmutableProductCreateDetails.class)))
                .thenReturn(createDetails);
        when(productService.create(createDetails)).thenReturn(createdId);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(createdId.toString())));

        verify(conversionService).convert(any(ProductCreateRequest.class), eq(ImmutableProductCreateDetails.class));
        verify(productService).create(createDetails);
    }

    @Test
    void createShouldReturnExceptionWhenRequestIsInvalid() throws Exception {
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("Product1")
                .productNumber(BigInteger.valueOf(1111))
                .info("Test Info")
                .category(Category.BOOKS)
                .price(BigDecimal.valueOf(1111))
                .amount(BigDecimal.valueOf(-1111))
                .build();

        ImmutableProductCreateDetails createDetails = ImmutableProductCreateDetails.builder()
                .name(request.getName())
                .productNumber(request.getProductNumber())
                .info(request.getInfo())
                .category(request.getCategory())
                .price(request.getPrice())
                .amount(request.getAmount())
                .lastUpdateTime(LocalDateTime.now())
                .build();

        when(conversionService.convert(any(ProductCreateRequest.class), eq(ImmutableProductCreateDetails.class)))
                .thenReturn(createDetails);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Amount must be 0 or greater!"));
    }

    @Test
    void findByIdShouldReturnProductDetailsWhenProductExists() throws Exception {
        UUID id = UUID.randomUUID();

        ProductData productData = ProductData.builder()
                .id(id)
                .name("Product1")
                .productNumber(BigInteger.valueOf(1111))
                .info("Info1")
                .category(Category.BOOKS)
                .price(BigDecimal.valueOf(1111))
                .amount(BigDecimal.valueOf(1111))
                .lastAmountUpdate(LocalDateTime.now())
                .creationTime(LocalDate.now())
                .build();

        ProductDataResponse response = ProductDataResponse.builder()
                .id(productData.getId())
                .name(productData.getName())
                .productNumber(productData.getProductNumber())
                .info(productData.getInfo())
                .category(productData.getCategory())
                .price(productData.getPrice())
                .amount(productData.getAmount())
                .lastAmountUpdate(productData.getLastAmountUpdate())
                .creationTime(productData.getCreationTime())
                .build();

        when(productService.findById(id)).thenReturn(productData);
        when(conversionService.convert(any(ProductData.class), eq(ProductDataResponse.class))).thenReturn(response);

        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productData.getId().toString()))
                .andExpect(jsonPath("$.name").value(productData.getName()));

        verify(productService).findById(id);
        verify(conversionService).convert(any(ProductData.class), eq(ProductDataResponse.class));
    }

    @Test
    void findByIdShouldReturnExceptionWhenProductNotExists() throws Exception {
        UUID id = UUID.randomUUID();

        when(productService.findById(id)).thenThrow(new ProductNotFoundException(id));

        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("The product with ID " + id + " was not found."));
    }

    @Test
    void updateShouldReturnUpdatedProductIdWhenRequestIsValid() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductUpdateRequest updateRequest = ProductUpdateRequest.builder()
                .productNumber(BigInteger.valueOf(2312))
                .info("Updated Info")
                .category(Category.ELECTRONICS)
                .price(BigDecimal.valueOf(1234))
                .amount(BigDecimal.valueOf(10))
                .build();

        ImmutableProductUpdateDetails updateDetails = ImmutableProductUpdateDetails.builder()
                .productNumber(updateRequest.getProductNumber())
                .info(updateRequest.getInfo())
                .category(updateRequest.getCategory())
                .price(updateRequest.getPrice())
                .amount(updateRequest.getAmount())
                .build();

        when(conversionService.convert(any(ProductUpdateRequest.class), eq(ImmutableProductUpdateDetails.class)))
                .thenReturn(updateDetails);
        when(productService.update(productId, updateDetails)).thenReturn(productId);

        mockMvc.perform(put(BASE_URL + "/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productId.toString())));

        verify(conversionService).convert(any(ProductUpdateRequest.class), eq(ImmutableProductUpdateDetails.class));
        verify(productService).update(productId, updateDetails);
    }

    @Test
    void updateShouldReturnExceptionWhenRequestIsNotValid() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductUpdateRequest updateRequest = ProductUpdateRequest.builder()
                .productNumber(BigInteger.valueOf(2312))
                .info("Updated Info")
                .category(Category.ELECTRONICS)
                .price(BigDecimal.valueOf(-1234))
                .amount(BigDecimal.valueOf(10))
                .build();

        ImmutableProductUpdateDetails updateDetails = ImmutableProductUpdateDetails.builder()
                .productNumber(updateRequest.getProductNumber())
                .info(updateRequest.getInfo())
                .category(updateRequest.getCategory())
                .price(updateRequest.getPrice())
                .amount(updateRequest.getAmount())
                .build();

        when(conversionService.convert(any(ProductUpdateRequest.class), eq(ImmutableProductUpdateDetails.class)))
                .thenReturn(updateDetails);

        mockMvc.perform(put(BASE_URL + "/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Price must be 0 or greater!"));
    }

    @Test
    void findAllShouldReturnPagedProductsWhenProductsExist() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        ProductData productData1 = ProductData.builder()
                .id(id1)
                .name("Product1")
                .productNumber(BigInteger.valueOf(1111))
                .info("Info1")
                .category(Category.BOOKS)
                .price(BigDecimal.valueOf(1111))
                .amount(BigDecimal.valueOf(1111))
                .lastAmountUpdate(LocalDateTime.now())
                .creationTime(LocalDate.now())
                .build();
        ProductData productData2 = ProductData.builder()
                .id(id2)
                .name("Product2")
                .productNumber(BigInteger.valueOf(2222))
                .info("Info2")
                .category(Category.BOOKS)
                .price(BigDecimal.valueOf(2222))
                .amount(BigDecimal.valueOf(2222))
                .lastAmountUpdate(LocalDateTime.now())
                .creationTime(LocalDate.now())
                .build();

        ProductDataResponse response1 = ProductDataResponse.builder()
                .id(productData1.getId())
                .name(productData1.getName())
                .productNumber(productData1.getProductNumber())
                .info(productData1.getInfo())
                .category(productData1.getCategory())
                .price(productData1.getPrice())
                .amount(productData1.getAmount())
                .lastAmountUpdate(productData1.getLastAmountUpdate())
                .creationTime(productData1.getCreationTime())
                .build();
        ProductDataResponse response2 = ProductDataResponse.builder()
                .id(productData2.getId())
                .name(productData2.getName())
                .productNumber(productData2.getProductNumber())
                .info(productData2.getInfo())
                .category(productData2.getCategory())
                .price(productData2.getPrice())
                .amount(productData2.getAmount())
                .lastAmountUpdate(productData2.getLastAmountUpdate())
                .creationTime(productData2.getCreationTime())
                .build();

        Pageable pageable = PageRequest.of(0, 2);
        Page<ProductData> productPage = new PageImpl<>(List.of(productData1, productData2), pageable, 2);

        when(productService.findAll(any(Pageable.class))).thenReturn(productPage);
        when(conversionService.convert(productData1, ProductDataResponse.class)).thenReturn(response1);
        when(conversionService.convert(productData2, ProductDataResponse.class)).thenReturn(response2);

        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(response1.getId().toString()))
                .andExpect(jsonPath("$.content[0].name").value(response1.getName()))
                .andExpect(jsonPath("$.content[1].id").value(response2.getId().toString()))
                .andExpect(jsonPath("$.content[1].name").value(response2.getName()));

        verify(productService).findAll(any(Pageable.class));
        verify(conversionService).convert(productData1, ProductDataResponse.class);
        verify(conversionService).convert(productData2, ProductDataResponse.class);
    }


    @Test
    void deleteShouldReturnNoContentWhenProductDeleted() throws Exception {
        UUID productId = UUID.randomUUID();

        mockMvc.perform(delete(BASE_URL + "/{id}", productId))
                .andExpect(status().isNoContent());

        verify(productService).delete(productId);
    }

    @Test
    void deleteShouldReturnExceptionWhenProductNotExists() throws Exception {
        UUID productId = UUID.randomUUID();

        doThrow(new ProductNotFoundException(productId)).when(productService).delete(productId);

        mockMvc.perform(delete(BASE_URL + "/{id}", productId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("The product with ID " + productId + " was not found."));
    }


}