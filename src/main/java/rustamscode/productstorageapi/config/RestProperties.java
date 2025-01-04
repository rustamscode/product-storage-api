package rustamscode.productstorageapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rest")
public class RestProperties {

  private CurrencyServiceClient currencyServiceClient;

  @Data
  public static class CurrencyServiceClient {
    private String host;
    private Methods methods;
    private Files Files;
    private Cache cache;
  }

  @Data
  public static class Methods {
    private String getCurrencyRate;
  }

  @Data
  public static class Files {
    private String defaultCurrencyRates;
  }

  @Data
  public static class Cache {
    private Integer expirationTime;
    private Integer initialCapacity;
  }
}
