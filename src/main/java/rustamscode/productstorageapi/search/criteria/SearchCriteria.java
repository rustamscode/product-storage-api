package rustamscode.productstorageapi.search.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        visible = true,
        include = JsonTypeInfo.As.PROPERTY,
        property = "field"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BigDecimalSearchCriteria.class, name = "price"),
        @JsonSubTypes.Type(value = CategorySearchCriteria.class, name = "category"),
        @JsonSubTypes.Type(value = LocalDateTimeSearchCriteria.class, name = "creationTime"),
        @JsonSubTypes.Type(value = StringSearchCriteria.class, name = "name"),
        @JsonSubTypes.Type(value = StringSearchCriteria.class, name = "info"),
        @JsonSubTypes.Type(value = UuidSearchCriteria.class, name = "id")
})
public interface SearchCriteria<T> {

    Predicate toPredicate(CriteriaBuilder builder, Root root);
}
