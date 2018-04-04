package com.my.todo.dao.category;

import com.my.todo.dao.AbstractDao;
import com.my.todo.model.Category;

import java.util.List;

/**
 * Created by marcin on 30.11.15.
 */
public interface CategoryDao extends AbstractDao<Category> {
    List<Category> findByName(String name);
    Category findByTaskId(Integer id);
    List<Category> findAllWithTasks();

}
