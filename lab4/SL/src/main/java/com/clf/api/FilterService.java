package com.clf.api;

import java.util.List;

public interface FilterService<T, F> {
    List<T> applyFilters(F filter, Class<T> entityClass);
}
