package com.my.todo.dao;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by marcin on 09.11.15.
 */

public interface AbstractDao<T> {
    void create(T entity);

    void edit(T entity);

    void remove(T entity);

    T find(Object id);

    List<T> findAll();

    List<T> findRange(int[] range);

    int count();
    EntityManager getEntityManager();
}
