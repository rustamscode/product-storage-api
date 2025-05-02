package rustamscode.productstorageapi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.config.RestProperties;
import rustamscode.productstorageapi.interaction.dto.CurrencyRateDetails;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultJsonValueLoader {

  final ObjectMapper objectMapper;

  final RestProperties restProperties;

  public CurrencyRateDetails getCurrencyRateDetails() {
    final String defaultCurrencyRates = restProperties.getCurrencyServiceClient()
        .getFiles()
        .getDefaultCurrencyRates();

    try {
      return objectMapper.readValue(
          new File(defaultCurrencyRates),
          CurrencyRateDetails.class
      );
    } catch (IOException e) {
      log.error("Error reading default value from file: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}

