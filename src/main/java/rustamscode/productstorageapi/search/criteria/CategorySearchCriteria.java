package rustamscode.productstorageapi.search.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import rustamscode.productstorageapi.exception.UnsupportedOperationTypeException;
import rustamscode.productstorageapi.persistance.enumeration.Category;
import rustamscode.productstorageapi.search.enumeration.OperationType;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategorySearchCriteria implements SearchCriteria<Category> {

    final String field;

    @NotNull(message = "The value must not be null!")
    final Category value;

    final String operationType;

    @Override
    public Predicate toPredicate(CriteriaBuilder builder, Root root) {
        Predicate predicate;

        switch (OperationType.fromOperation(operationType)) {
            case EQUAL, LIKE, GREATER_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL ->
                    predicate = builder.equal(root.get(field), value);
            default -> throw new UnsupportedOperationTypeException(operationType, field);
        }
        return predicate;
    }
}

