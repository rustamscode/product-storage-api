package rustamscode.productstorageapi.search.criteria;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import rustamscode.productstorageapi.search.enumeration.OperationType;
import rustamscode.productstorageapi.search.strategy.PredicateStrategy;
import rustamscode.productstorageapi.search.strategy.StringPredicateStrategy;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StringSearchCriteria implements SearchCriteria<String> {

    final static PredicateStrategy STRATEGY = new StringPredicateStrategy();

    final String field;

    @NotBlank(message = "The value must not be null or blank!")
    final String value;

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
