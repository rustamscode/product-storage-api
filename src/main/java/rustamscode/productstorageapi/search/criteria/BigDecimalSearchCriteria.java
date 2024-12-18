package rustamscode.productstorageapi.search.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import rustamscode.productstorageapi.exception.UnsupportedOperationTypeException;
import rustamscode.productstorageapi.search.enumeration.OperationType;

import java.math.BigDecimal;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BigDecimalSearchCriteria implements SearchCriteria<BigDecimal> {
    final String field;

    @NotNull
    @DecimalMin(value = "0.00", message = "The value must be 0 or greater!")
    final BigDecimal value;

    final String operationType;

    @Override
    public Predicate toPredicate(CriteriaBuilder builder, Root root) {
        Predicate predicate;

        switch (OperationType.fromOperation(operationType)) {
            case EQUAL -> predicate = builder.equal(root.get(field), value);
            case LIKE -> predicate = builder.and(
                    builder.greaterThanOrEqualTo(root.get(field), value.multiply(BigDecimal.valueOf(0.9))),
                    builder.lessThanOrEqualTo(root.get(field), value.multiply(BigDecimal.valueOf(1.1)))
            );
            case GREATER_THAN_OR_EQUAL -> predicate = builder.greaterThanOrEqualTo(root.get(field), value);
            case LESS_THAN_OR_EQUAL -> predicate = builder.lessThanOrEqualTo(root.get(field), value);
            default -> throw new UnsupportedOperationTypeException(operationType, field);
        }
        return predicate;
    }
}
