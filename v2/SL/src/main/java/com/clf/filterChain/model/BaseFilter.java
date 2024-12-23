package com.clf.filterChain.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public interface BaseFilter<T> {
    void createFilterChain();

    void applyFilter(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root);
}