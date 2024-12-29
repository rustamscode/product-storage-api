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
}
