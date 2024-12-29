package rustamscode.productstorageapi.search.strategy;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface PredicateStrategy<T> {
    Predicate getEqualsPredicate(Expression<T> expression, T value, CriteriaBuilder cb);

    Predicate getLikePredicate(Expression<T> expression, T value, CriteriaBuilder cb);

    Predicate getGreaterThanOrEqualsPredicate(Expression<T> expression, T value, CriteriaBuilder cb);

    Predicate getLessOrEqualsPredicate(Expression<T> expression, T value, CriteriaBuilder cb);

}
