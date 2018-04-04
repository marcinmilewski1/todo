package com.my.todo.service.category;

import com.my.todo.dao.category.CategoryDao;
import com.my.todo.dao.category.CategoryDaoImpl;
import com.my.todo.model.Category;

import java.util.List;

/**
 * Created by marcin on 30.11.15.
 */

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryFacade;

    public CategoryServiceImpl() {
        categoryFacade = new CategoryDaoImpl();
    }

    @Override
    public void addCategory(Category category) {
        categoryFacade.create(category);
    }

    @Override
    public void removeCategory(Integer id) {
        Category category = categoryFacade.find(id);
        categoryFacade.remove(category);
    }

    @Override
    public void removeAllTasks(Integer categoryId) {
        Category category = categoryFacade.find(categoryId);
        category.getTaskList().clear();
        categoryFacade.edit(category);
    }

    @Override
    public List<Category> findByName(String name) {
        return categoryFacade.findByName(name);
    }

    @Override
    public Category findById(Integer id) {
        return categoryFacade.find(id);
    }

    @Override
    public Category findByTaskId(Integer id) {
        return categoryFacade.findByTaskId(id);
    }

    @Override
    public void edit(Category category) {
        categoryFacade.edit(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryFacade.findAll();
    }

    @Override
    public List<Category> findAllWithTasks() {
        return categoryFacade.findAllWithTasks();
    }
}
