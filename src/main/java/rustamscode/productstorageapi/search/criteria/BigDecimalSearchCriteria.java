package rustamscode.productstorageapi.search.criteria;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import rustamscode.productstorageapi.search.enumeration.OperationType;
import rustamscode.productstorageapi.search.strategy.BigDecimalPredicateStrategy;
import rustamscode.productstorageapi.search.strategy.PredicateStrategy;

import java.math.BigDecimal;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BigDecimalSearchCriteria implements SearchCriteria<BigDecimal> {

    final static PredicateStrategy STRATEGY = new BigDecimalPredicateStrategy();

    final String field;

    @NotNull
    @DecimalMin(value = "0.00", message = "The value must be 0 or greater!")
    final BigDecimal value;

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
