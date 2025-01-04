package rustamscode.productstorageapi.search.criteria;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import rustamscode.productstorageapi.enumeration.Category;
import rustamscode.productstorageapi.enumeration.OperationType;
import rustamscode.productstorageapi.search.strategy.CategoryPredicateStrategy;
import rustamscode.productstorageapi.search.strategy.PredicateStrategy;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategorySearchCriteria implements SearchCriteria<Category> {

    final static PredicateStrategy STRATEGY = new CategoryPredicateStrategy();

    final String field;

    @NotNull(message = "The value must not be null!")
    final Category value;

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

