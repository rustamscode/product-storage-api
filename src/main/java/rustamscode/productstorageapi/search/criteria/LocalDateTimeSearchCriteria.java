package rustamscode.productstorageapi.search.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import rustamscode.productstorageapi.enumeration.OperationType;
import rustamscode.productstorageapi.search.strategy.LocalDataTimePredicateStrategy;
import rustamscode.productstorageapi.search.strategy.PredicateStrategy;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocalDateTimeSearchCriteria implements SearchCriteria<LocalDateTime> {

  final static PredicateStrategy STRATEGY = new LocalDataTimePredicateStrategy();

  final String field;

  @PastOrPresent
  @NotNull(message = "The value must not be null!")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  final LocalDateTime value;

  final String operationType;

  @Override
  public PredicateStrategy getStrategy() {
    return STRATEGY;
  }

  @Override
  public OperationType getOperationType() {
    return OperationType.fromOperation(operationType);
  }
}
