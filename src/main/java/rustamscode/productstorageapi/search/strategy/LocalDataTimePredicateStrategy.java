package rustamscode.productstorageapi.search.strategy;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;

public class LocalDataTimePredicateStrategy implements PredicateStrategy<LocalDateTime> {

  @Override
  public Predicate getEqualsPredicate(
      Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder builder
  ) {
    return builder.equal(builder.function("date", LocalDateTime.class, expression), value.toLocalDate());
  }

  @Override
  public Predicate getLikePredicate(
      Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder builder
  ) {
    return builder.between(expression, value.minusDays(2), value.plusDays(2));
  }

  @Override
  public Predicate getGreaterThanOrEqualsPredicate(
      Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder builder
  ) {
    return builder.greaterThanOrEqualTo(expression, value);
  }

  @Override
  public Predicate getLessOrEqualsPredicate(
      Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder builder
  ) {
    return builder.lessThanOrEqualTo(expression, value);
  }
}
