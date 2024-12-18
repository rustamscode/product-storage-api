package rustamscode.productstorageapi.search.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import rustamscode.productstorageapi.exception.UnsupportedOperationTypeException;
import rustamscode.productstorageapi.search.enumeration.OperationType;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocalDateTimeSearchCriteria implements SearchCriteria<LocalDateTime> {
    final String field;

    @PastOrPresent
    @NotNull(message = "The value must not be null!")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    final LocalDateTime value;

    final String operationType;

    @Override
    public Predicate toPredicate(CriteriaBuilder builder, Root root) {
        Predicate predicate;

        switch (OperationType.fromOperation(operationType)) {
            case EQUAL ->
                    predicate = builder.equal(builder.function("date", LocalDateTime.class, root.get(field)), value.toLocalDate());
            case LIKE -> predicate = builder.between(root.get(field), value.minusDays(2), value.plusDays(2));
            case GREATER_THAN_OR_EQUAL -> predicate = builder.greaterThanOrEqualTo(root.get(field), value);
            case LESS_THAN_OR_EQUAL -> predicate = builder.lessThanOrEqualTo(root.get(field), value);
            default -> throw new UnsupportedOperationTypeException(operationType, field);
        }
        return predicate;
    }
}
