package rustamscode.productstorageapi.search.criteria;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import rustamscode.productstorageapi.search.enumeration.OperationType;
import rustamscode.productstorageapi.search.strategy.PredicateStrategy;
import rustamscode.productstorageapi.search.strategy.UuidPredicateStrategy;

import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UuidSearchCriteria implements SearchCriteria<UUID> {

    final static PredicateStrategy STRATEGY = new UuidPredicateStrategy();

    final String field;

    @NotNull(message = "The value must not be null!")
    final UUID value;

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
