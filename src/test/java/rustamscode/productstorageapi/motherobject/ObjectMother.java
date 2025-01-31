package rustamscode.productstorageapi.motherobject;

public class ObjectMother {

  public static ProductEntityBuilder productEntity() {
    return ProductEntityBuilder.getInstance();
  }

  public static ProductCreateRequestBuilder productCreateRequest() {
    return ProductCreateRequestBuilder.getInstance();
  }

  public static ImmutableProductCreateDetailsBuilder immutableProductCreateDetails() {
    return ImmutableProductCreateDetailsBuilder.getInstance();
  }

  public static ProductDataBuilder productData() {
    return ProductDataBuilder.getInstance();
  }

  public static ProductDataResponseBuilder productDataResponse() {
    return ProductDataResponseBuilder.getInstance();
  }

  public static ProductUpdateRequestBuilder productUpdateRequest() {
    return ProductUpdateRequestBuilder.getInstance();
  }

  public static ImmutableProductUpdateDetailsBuilder immutableProductUpdateDetails() {
    return ImmutableProductUpdateDetailsBuilder.getInstance();
  }

  public static CurrencyRateDetailsBuilder currencyRateDetails() {
    return CurrencyRateDetailsBuilder.getInstance();
  }

  public static OrderedProductRequestBuilder orderedProductRequest() {
    return OrderedProductRequestBuilder.getInstance();
  }

  public static ProductOrderRequestBuilder productOrderRequest() {
    return ProductOrderRequestBuilder.getInstance();
  }

  public static OrderedProductDataResponseBuilder orderedProductDataResponse() {
    return OrderedProductDataResponseBuilder.getInstance();
  }

  public static OrderDataResponseBuilder orderDataResponse() {
    return OrderDataResponseBuilder.getInstance();
  }

  public static OrderDataBuilder orderData() {
    return OrderDataBuilder.getInstance();
  }

  public static OrderedProductDataObjectBuilder orderedProductDataObject() {
    return OrderedProductDataObjectBuilder.getInstance();
  }

  public static ImmutableOrderedProductObjectBuilder immutableOrderedProductObject() {
    return ImmutableOrderedProductObjectBuilder.getInstance();
  }

  public static CustomerEntityBuilder customerEntity() {
    return CustomerEntityBuilder.getInstance();
  }

  public static OrderEntityBuilder orderEntity() {
    return OrderEntityBuilder.getInstance();
  }
}
