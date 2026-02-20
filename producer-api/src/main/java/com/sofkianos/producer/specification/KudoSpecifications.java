package com.sofkianos.producer.specification;

import com.sofkianos.producer.dto.KudoSearchCriteria;
import com.sofkianos.producer.entity.Kudo;
import com.sofkianos.producer.entity.KudoCategory;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specifications for dynamic Kudo query building.
 */
public final class KudoSpecifications {

    private KudoSpecifications() {
        // utility class
    }

    /**
     * Builds a {@link Specification} from the given search criteria.
     *
     * @param criteria the search criteria
     * @return a composed specification matching all provided filters
     */
    public static Specification<Kudo> fromCriteria(KudoSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getCategory() != null && !criteria.getCategory().isBlank()) {
                try {
                    KudoCategory cat = KudoCategory.fromString(criteria.getCategory());
                    predicates.add(cb.equal(root.get("category"), cat));
                } catch (IllegalArgumentException ignored) {
                    // unknown category → no results for that filter
                    predicates.add(cb.disjunction());
                }
            }

            if (criteria.getSearchText() != null && !criteria.getSearchText().isBlank()) {
                String escaped = criteria.getSearchText()
                        .replace("\\", "\\\\")
                        .replace("%", "\\%")
                        .replace("_", "\\_");
                String pattern = "%" + escaped.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("message")), pattern),
                        cb.like(cb.lower(root.get("toUser")), pattern),
                        cb.like(cb.lower(root.get("fromUser")), pattern)
                ));
            }

            if (criteria.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), criteria.getStartDate()));
            }

            if (criteria.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), criteria.getEndDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
