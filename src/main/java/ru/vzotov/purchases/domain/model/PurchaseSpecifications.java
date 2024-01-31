package ru.vzotov.purchases.domain.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ru.vzotov.purchase.domain.model.Purchase;

import java.time.Instant;

public class PurchaseSpecifications {
    public static Specification<Purchase> updatedAfter(Instant timestamp) {
        return new Specification<Purchase>() {
            @Override
            public Predicate toPredicate(Root<Purchase> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.greaterThan(root.get("updatedOn"), timestamp);
            }
        };
    }
}
