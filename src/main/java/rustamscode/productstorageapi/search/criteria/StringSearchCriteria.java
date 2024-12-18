package rustamscode.productstorageapi.search.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import rustamscode.productstorageapi.exception.UnsupportedOperationTypeException;
import rustamscode.productstorageapi.search.enumeration.OperationType;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StringSearchCriteria implements SearchCriteria<String> {

    final String field;

    @NotBlank(message = "The value must not be null or blank!")
    final String value;

    final String operationType;

    @Override
    public Predicate toPredicate(CriteriaBuilder builder, Root root) {
        Predicate predicate;

        switch (OperationType.fromOperation(operationType)) {
            case EQUAL -> predicate = builder.equal(root.get(field), value);
            case LIKE -> predicate = builder.like(builder.lower(root.get(field)), "%" + value.toLowerCase() + "%");
            case GREATER_THAN_OR_EQUAL -> predicate = builder.like(root.get(field), "%" + value);
            case LESS_THAN_OR_EQUAL -> predicate = builder.like(root.get(field), value + "%");
            default -> throw new UnsupportedOperationTypeException(operationType, field);
        }
        return predicate;
    }
}
