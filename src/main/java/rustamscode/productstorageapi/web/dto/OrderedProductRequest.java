package rustamscode.productstorageapi.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderedProductRequest {

  @NotNull
  UUID id;

  @NotNull
  @Min(value = 1, message = "Amount must be 1 or greater!")
  BigDecimal amount;
}
