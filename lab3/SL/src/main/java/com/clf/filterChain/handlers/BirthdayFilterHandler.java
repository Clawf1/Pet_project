package com.clf.filterChain.handlers;

import com.clf.filterChain.AbstractFilterHandler;
import com.clf.filterChain.model.BaseFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Date;
import java.util.function.Function;

public class BirthdayFilterHandler<T, F extends BaseFilter<T>> extends AbstractFilterHandler<T, F> {
    private final Function<F, Date> birthdayGetter;

    public BirthdayFilterHandler(Function<F, Date> birthdayGetter) {
        this.birthdayGetter = birthdayGetter;
    }

    @Override
    public void applyFilter(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, F filter) {
        Date birthday = birthdayGetter.apply(filter);
        if (birthday != null) {
            Predicate currentPredicate = cb.equal(root.get("birthday"), birthday);
            if (cq.getRestriction() != null) {
                cq.where(cb.and(cq.getRestriction(), currentPredicate));
            } else {
                cq.where(currentPredicate);
            }
        }
        super.applyFilter(cb, cq, root, filter);
    }
}
