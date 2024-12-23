package com.clf.filterChain;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public interface FilterHandler<T, F> {
    void applyFilter(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, F filter);
    void setNext(FilterHandler<T, F> next);
}
