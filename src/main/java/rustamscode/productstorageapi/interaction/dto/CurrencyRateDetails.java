package rustamscode.productstorageapi.interaction.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CurrencyRateDetails {
  private BigDecimal RUB;

  private BigDecimal USD;

  private BigDecimal CNY;
}
