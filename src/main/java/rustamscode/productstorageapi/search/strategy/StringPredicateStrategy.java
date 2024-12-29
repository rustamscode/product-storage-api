package rustamscode.productstorageapi.search.strategy;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public class StringPredicateStrategy implements PredicateStrategy<String> {

    @Override
    public Predicate getEqualsPredicate(Expression<String> expression, String value, CriteriaBuilder builder) {
        return builder.equal(expression, value);
    }

    @Override
    public Predicate getLikePredicate(Expression<String> expression, String value, CriteriaBuilder builder) {
        return builder.like(builder.lower(expression), "%" + value.toLowerCase() + "%");
    }

    @Override
    public Predicate getGreaterThanOrEqualsPredicate(
            Expression<String> expression, String value, CriteriaBuilder builder
    ) {
        return builder.like(expression, "%" + value);
    }

    @Override
    public Predicate getLessOrEqualsPredicate(Expression<String> expression, String value, CriteriaBuilder builder) {
        return builder.like(expression, value + "%");
    }
}
