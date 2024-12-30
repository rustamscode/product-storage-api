package rustamscode.productstorageapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import rustamscode.productstorageapi.web.dto.request.ProductOrderRequest;

import java.util.UUID;

@Tag(name = "Order management API")
@Validated
public interface OrderController {

  @PostMapping
  @Operation(summary = "Create a order")
  @ResponseStatus(HttpStatus.CREATED)
  UUID create(@RequestHeader final Long customerId,
              @Valid @NotNull @RequestBody final ProductOrderRequest request);
}
