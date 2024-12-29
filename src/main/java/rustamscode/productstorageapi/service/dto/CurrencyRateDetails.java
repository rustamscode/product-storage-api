package rustamscode.productstorageapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRateDetails {
  private BigDecimal RUB;

  private BigDecimal USD;

  private BigDecimal CNY;
}
