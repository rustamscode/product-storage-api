package rustamscode.productstorageapi.search.strategy;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.math.BigDecimal;

public class BigDecimalPredicateStrategy implements PredicateStrategy<BigDecimal> {

  @Override
  public Predicate getEqualsPredicate(Expression<BigDecimal> expression, BigDecimal value, CriteriaBuilder builder) {
    return builder.equal(expression, value);
  }

  @Override
  public Predicate getLikePredicate(Expression<BigDecimal> expression, BigDecimal value, CriteriaBuilder builder) {
    return builder.and(
        builder.greaterThanOrEqualTo(expression, value.multiply(BigDecimal.valueOf(0.9))),
        builder.lessThanOrEqualTo(expression, value.multiply(BigDecimal.valueOf(1.1)))
    );
  }

  @Override
  public Predicate getGreaterThanOrEqualsPredicate(
      Expression<BigDecimal> expression, BigDecimal value, CriteriaBuilder builder
  ) {
    return builder.greaterThanOrEqualTo(expression, value);
  }

  @Override
  public Predicate getLessOrEqualsPredicate(
      Expression<BigDecimal> expression, BigDecimal value, CriteriaBuilder builder
  ) {
    return builder.lessThanOrEqualTo(expression, value);
  }
}
