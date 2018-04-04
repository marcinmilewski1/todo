package com.my.todo.dao.category;

import com.my.todo.dao.AbstractDaoImpl;
import com.my.todo.model.Category;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by marcin on 30.11.15.
 */
public class CategoryDaoImpl extends AbstractDaoImpl<Category> implements CategoryDao {
    public CategoryDaoImpl() {
        super(Category.class);
    }

    @Override
    public List<Category> findByName(String name) {

        TypedQuery<Category> query =
                getEntityManager().createNamedQuery("Category.findByName", Category.class);
        query.setParameter("name", name);
        List<Category> results = query.getResultList();
        return results;
    }

    @Override
    public Category findByTaskId(Integer id) {
        TypedQuery<Category> query =
                getEntityManager().createNamedQuery("Category.findByTaskId", Category.class);
        query.setParameter("id", id);
        Category result = query.getSingleResult();
        return result;
    }

    @Override
    public List<Category> findAllWithTasks() {
        TypedQuery<Category> query =
                getEntityManager().createNamedQuery("Category.findAllWithTasks", Category.class);
        List<Category> results = query.getResultList();
        return results;
    }



}
