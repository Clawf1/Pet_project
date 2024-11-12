package com.clf.filterChain;

import com.clf.filterChain.model.BaseFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public abstract class AbstractFilterHandler<T, F extends BaseFilter<T>> implements FilterHandler<T, F> {
    private FilterHandler<T, F> next;

    @Override
    public void setNext(FilterHandler<T, F> next) {
        this.next = next;
    }

    @Override
    public void applyFilter(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, F filter) {
        if (next != null) {
            next.applyFilter(cb, cq, root, filter);
        }
    }
}
