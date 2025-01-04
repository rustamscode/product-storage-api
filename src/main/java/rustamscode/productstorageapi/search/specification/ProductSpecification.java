package rustamscode.productstorageapi.search.specification;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import rustamscode.productstorageapi.persistance.entity.ProductEntity;
import rustamscode.productstorageapi.search.criteria.SearchCriteria;

import java.util.List;
import java.util.stream.Collectors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSpecification {

    public Specification<ProductEntity> generateSpecification(List<SearchCriteria> criteriaList) {
        List<Specification<ProductEntity>> specifications = criteriaList.stream()
                .map(this::getSpecification)
                .collect(Collectors.toList());

        return Specification.allOf(specifications);
    }

    private Specification<ProductEntity> getSpecification(SearchCriteria criteria) {
        return (root, query, builder) ->
                switch (criteria.getOperationType()) {
                    case EQUAL -> criteria.getStrategy()
                            .getEqualsPredicate(root.get(criteria.getField()), criteria.getValue(), builder);
                    case LIKE -> criteria.getStrategy()
                            .getLikePredicate(root.get(criteria.getField()), criteria.getValue(), builder);
                    case GREATER_THAN_OR_EQUAL -> criteria.getStrategy()
                            .getGreaterThanOrEqualsPredicate(root.get(criteria.getField()), criteria.getValue(), builder);
                    case LESS_THAN_OR_EQUAL -> criteria.getStrategy()
                            .getLessOrEqualsPredicate(root.get(criteria.getField()), criteria.getValue(), builder);
                };
    }
}
