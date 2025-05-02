package rustamscode.productstorageapi.search.strategy;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import rustamscode.productstorageapi.enumeration.Category;

public class CategoryPredicateStrategy implements PredicateStrategy<Category> {

  @Override
  public Predicate getEqualsPredicate(Expression<Category> expression, Category value, CriteriaBuilder builder) {
    return builder.equal(expression, value);
  }

  @Override
  public Predicate getLikePredicate(Expression<Category> expression, Category value, CriteriaBuilder builder) {
    return builder.equal(expression, value);
  }

  @Override
  public Predicate getGreaterThanOrEqualsPredicate(
      Expression<Category> expression, Category value, CriteriaBuilder builder
  ) {
    return builder.equal(expression, value);
  }

  @Override
  public Predicate getLessOrEqualsPredicate(
      Expression<Category> expression, Category value, CriteriaBuilder builder
  ) {
    return builder.equal(expression, value);
  }
}
