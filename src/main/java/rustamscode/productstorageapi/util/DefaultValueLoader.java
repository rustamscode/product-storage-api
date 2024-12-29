package rustamscode.productstorageapi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.service.dto.CurrencyRateDetails;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultValueLoader {

  final ObjectMapper objectMapper;

  @Value("${currency-service.files.default-currency-rates}")
  String DEFAULT_CURRENCY_RATE_FILE;

  public CurrencyRateDetails getCurrencyRateDetails() {
    try {
      return objectMapper.readValue(
          new File(DEFAULT_CURRENCY_RATE_FILE),
          CurrencyRateDetails.class
      );
    } catch (IOException e) {
      log.error("Error reading default value from file: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}

