package rustamscode.productstorageapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import rustamscode.productstorageapi.enumeration.Currency;
import rustamscode.productstorageapi.search.criteria.SearchCriteria;
import rustamscode.productstorageapi.web.dto.ProductCreateRequest;
import rustamscode.productstorageapi.web.dto.ProductDataResponse;
import rustamscode.productstorageapi.web.dto.ProductUpdateRequest;

import java.util.List;
import java.util.UUID;

@Tag(name = "Product management API")
@Validated
public interface ProductController {

  @PostMapping
  @Operation(summary = "Create a product")
  @ResponseStatus(HttpStatus.CREATED)
  UUID create(@Valid @NotNull @RequestBody final ProductCreateRequest productCreateRequest);

  @Operation(summary = "Find a product by ID")
  @GetMapping("/{id}")
  ProductDataResponse findById(@RequestHeader final Currency currency,
                               @PathVariable final UUID id);

  @Operation(summary = "Find all products")
  @GetMapping
  Page<ProductDataResponse> findAll(@RequestHeader final Currency currency,
                                    @PageableDefault(sort = "name") final Pageable pageable);

  @Operation(summary = "Update a product")
  @PutMapping("/{id}")
  UUID update(@PathVariable final UUID id,
              @Valid @NotNull @RequestBody final ProductUpdateRequest productUpdateRequest);

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a product")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(@PathVariable final UUID id);

  @GetMapping("/search")
  @Operation(summary = "Search for product")
  Page<ProductDataResponse> search(@RequestHeader final Currency currency,
                                   @PageableDefault(sort = "name") final Pageable pageable,
                                   @Valid @RequestBody final List<SearchCriteria> criteriaList);
}
