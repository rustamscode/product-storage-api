package rustamscode.productstorageapi.web.dto.request;

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
  @Min(value = 0, message = "Product id must be 0 or greater!")
  UUID id;

  @NotNull
  @Min(value = 0, message = "Amount must be 0 or greater!")
  BigDecimal amount;
}
