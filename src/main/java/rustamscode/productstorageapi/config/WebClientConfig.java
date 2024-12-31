package rustamscode.productstorageapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

  final private RestProperties restProperties;

  @Bean
  public WebClient webClient() {
    final String baseUrl = restProperties.getCurrencyServiceClient().getHost();

    return WebClient.builder()
        .baseUrl(baseUrl)
        .build();
  }
}
