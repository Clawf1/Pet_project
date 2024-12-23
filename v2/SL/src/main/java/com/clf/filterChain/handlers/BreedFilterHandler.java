package com.clf.filterChain.handlers;

import com.clf.filterChain.AbstractFilterHandler;
import com.clf.filterChain.model.BaseFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.function.Function;

public class BreedFilterHandler<T, F extends BaseFilter<T>> extends AbstractFilterHandler<T, F> {

    private final Function<F, String> breedGetter;

    public BreedFilterHandler(Function<F, String> breedGetter) {
        this.breedGetter = breedGetter;
    }

    @Override
    public void applyFilter(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, F filter) {
        String breed = breedGetter.apply(filter);
        if (breed != null) {
            Predicate currentPredicate = cb.equal(root.get("breed"), breed);
            if (cq.getRestriction() != null) {
                cq.where(cb.and(cq.getRestriction(), currentPredicate));
            } else {
                cq.where(currentPredicate);
            }
        }
        super.applyFilter(cb, cq, root, filter);
    }
}