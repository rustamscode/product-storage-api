package rustamscode.productstorageapi.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import rustamscode.productstorageapi.exception.CurrencyRateFetchException;
import rustamscode.productstorageapi.service.dto.CurrencyRateDetails;

import static java.time.Duration.ofSeconds;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyServiceImpl implements CurrencyService {

  final WebClient webClient;

  @Value("${currency-service.methods.get-currency-rate}")
  String CURRENCY_URI;

  @Override
  @Cacheable(value = "currencyCache", unless = "#result == null")
  public CurrencyRateDetails getCurrencyRateDetails() {
    return webClient
        .get()
        .uri(CURRENCY_URI)
        .retrieve()
        .bodyToMono(CurrencyRateDetails.class)
        .retryWhen(Retry.fixedDelay(3, ofSeconds(2))
            .doBeforeRetry(
                signal -> log.info("Retry attempt: {}", signal.totalRetries()
                )))
        .onErrorMap(error -> {
          log.error("Error getting currency: {}", error.getMessage());
          return new CurrencyRateFetchException(error.getMessage());
        })
        .block();
  }

}