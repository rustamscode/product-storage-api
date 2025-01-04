package rustamscode.productstorageapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import rustamscode.productstorageapi.enumeration.OrderStatus;
import rustamscode.productstorageapi.web.dto.request.OrderedProductRequest;
import rustamscode.productstorageapi.web.dto.request.ProductOrderRequest;
import rustamscode.productstorageapi.web.dto.response.OrderDataResponse;

import java.util.List;
import java.util.UUID;

@Tag(name = "Order management API")
@Validated
public interface OrderController {

  @PostMapping
  @Operation(summary = "Create a order")
  @ResponseStatus(HttpStatus.CREATED)
  UUID create(@RequestHeader final Long customerId,
              @Valid @NotNull @RequestBody final ProductOrderRequest request);

  @PatchMapping("/{id}")
  @Operation(summary = "Update an order")
  UUID update(@PathVariable final UUID id,
              @RequestHeader final Long customerId,
              @Valid @NotNull @RequestBody final List<OrderedProductRequest> orderedProducts);

  @GetMapping("/{id}")
  @Operation(summary = "Find an order by ID")
  OrderDataResponse findById(@PathVariable final UUID id,
                             @RequestHeader final Long customerId);

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an order")
  void delete(@PathVariable final UUID id,
              @RequestHeader final Long customerId);

  @PostMapping("/{id}/confirm")
  @Operation(summary = "Confirm an order")
  void confirm(@PathVariable final UUID id,
               @RequestHeader final Long customerId);

  @PatchMapping("{id}/status")
  @Operation(summary = "Check order status")
  UUID updateStatus(@PathVariable final UUID id,
                    @RequestHeader final Long customerId,
                    @RequestBody @NotNull final OrderStatus status);
}
