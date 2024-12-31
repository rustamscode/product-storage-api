package rustamscode.productstorageapi.interaction;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import rustamscode.productstorageapi.config.RestProperties;
import rustamscode.productstorageapi.exception.CurrencyRateFetchException;
import rustamscode.productstorageapi.interaction.dto.CurrencyRateDetails;

import static java.time.Duration.ofSeconds;

@Slf4j
@Profile("dev")
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyServiceClientImpl implements CurrencyServiceClient {

  final WebClient webClient;

  final RestProperties restProperties;

  @Override
  @Cacheable(value = "currencyCache", unless = "#result == null")
  public @Nullable CurrencyRateDetails getCurrencyRateDetails() {
    final String currencyUri = restProperties.getCurrencyServiceClient()
        .getMethods()
        .getGetCurrencyRate();

    try {
      return webClient
          .get()
          .uri(currencyUri)
          .retrieve()
          .bodyToMono(CurrencyRateDetails.class)
          .retryWhen(Retry.fixedDelay(3, ofSeconds(2))
              .doBeforeRetry(
                  signal -> log.info("Retry attempt: {}", signal.totalRetries()
                  )))
          .onErrorMap(error -> new CurrencyRateFetchException(error.getMessage()))
          .block();
    } catch (Exception e) {
      log.error("Error getting currency: {}", e.getMessage());
      return null;
    }
  }

}