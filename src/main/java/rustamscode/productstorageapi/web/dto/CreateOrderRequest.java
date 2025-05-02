package rustamscode.productstorageapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderRequest {
  @NotBlank
  @Size(min = 1, max = 64, message = "Address must not be blank or exceed 64 characters!")
  String deliveryAddress;

  @NotEmpty(message = "Product list must not be empty!")
  List<OrderedProductRequest> products;
}
