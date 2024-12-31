package rustamscode.productstorageapi.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "rest")
public class RestProperties {

  CurrencyServiceClient currencyServiceClient;

  @Data
  public static class CurrencyServiceClient {
    String host;
    Methods methods;
    Files Files;
    Cache cache;
  }

  @Data
  public static class Methods {
    String getCurrencyRate;
  }

  @Data
  public static class Files {
    String defaultCurrencyRates;
  }

  @Data
  public static class Cache {
    Integer expirationTime;
    Integer initialCapacity;
  }


}
