package com.clf.dao;

import java.util.List;

public interface Dao<T> {
    void save(T t);

    void update(T t);

    void delete(T t);

    void delete(Long id);

    T findById(Long id);

    List<T> findAll();
}
