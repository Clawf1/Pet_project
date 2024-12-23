package com.clf.service.filterChain.handlers;

import com.clf.service.filterChain.AbstractFilterHandler;
import com.clf.service.filterChain.model.BaseFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.function.Function;

public class OwnerFilterHandler<T, F extends BaseFilter<T>> extends AbstractFilterHandler<T, F> {
    private final Function<F, Long> ownerIdGetter;

    public OwnerFilterHandler(Function<F, Long> ownerIdGetter) {
        this.ownerIdGetter = ownerIdGetter;
    }

    @Override
    public void applyFilter(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, F filter) {
        Long ownerId = ownerIdGetter.apply(filter);
        if (ownerId != null) {
            Predicate currentPredicate = cb.equal(root.get("owner.id"), ownerId);
            if (cq.getRestriction() != null) {
                cq.where(cb.and(cq.getRestriction(), currentPredicate));
            } else {
                cq.where(currentPredicate);
            }
        }
        super.applyFilter(cb, cq, root, filter);
    }
}
