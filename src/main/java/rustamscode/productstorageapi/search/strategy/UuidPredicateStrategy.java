package rustamscode.productstorageapi.search.strategy;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.util.UUID;

public class UuidPredicateStrategy implements PredicateStrategy<UUID> {

  @Override
  public Predicate getEqualsPredicate(Expression<UUID> expression, UUID value, CriteriaBuilder builder) {
    return builder.equal(expression, value);
  }

  @Override
  public Predicate getLikePredicate(Expression<UUID> expression, UUID value, CriteriaBuilder builder) {
    return builder.equal(expression, value);
  }

  @Override
  public Predicate getGreaterThanOrEqualsPredicate(
      Expression<UUID> expression, UUID value, CriteriaBuilder builder
  ) {
    return builder.equal(expression, value);
  }

  @Override
  public Predicate getLessOrEqualsPredicate(Expression<UUID> expression, UUID value, CriteriaBuilder builder) {
    return builder.equal(expression, value);
  }
}
