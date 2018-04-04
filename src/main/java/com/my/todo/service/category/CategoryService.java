package com.my.todo.service.category;

import com.my.todo.model.Category;

import java.util.List;

/**
 * Created by marcin on 30.11.15.
 */
public interface CategoryService {
    void addCategory(Category category);

    void removeCategory(Integer id);

    void removeAllTasks(Integer categoryId);

    void edit(Category category);

    List<Category> findByName(String name);

    Category findById(Integer id);

    Category findByTaskId(Integer id);

    List<Category> findAll();

    List<Category> findAllWithTasks();
}
