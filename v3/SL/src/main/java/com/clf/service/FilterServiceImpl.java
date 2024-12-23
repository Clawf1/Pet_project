package com.clf.service;


import com.clf.api.FilterService;
import com.clf.service.filterChain.model.BaseFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilterServiceImpl<T, F extends BaseFilter<T>> implements FilterService<T, F> {
    private final EntityManager entityManager;

    public FilterServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<T> applyFilters(F filter, Class<T> entityClass) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);

        filter.applyFilter(cb, cq, root);

        return entityManager.createQuery(cq).getResultList();
    }
}