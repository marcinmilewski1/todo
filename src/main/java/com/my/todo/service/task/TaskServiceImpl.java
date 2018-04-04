package com.my.todo.service.task;

import com.my.todo.dao.task.TaskDao;
import com.my.todo.dao.task.TaskDaoImpl;
import com.my.todo.model.Task;
import com.my.todo.util.Priority;
import com.my.todo.util.TaskStatus;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by marcin on 30.11.15.
 */
public class TaskServiceImpl implements TaskService {

    private TaskDao taskFacade;

    public TaskServiceImpl() {
        taskFacade = new TaskDaoImpl();
    }

    @Override
    public void add(Task task) {
        taskFacade.create(task);
    }

    @Override
    public void remove(Integer id) {
        Task task = taskFacade.find(id);
        taskFacade.remove(task);
    }

    @Override
    public List<Task> findByName(String name) {
        return taskFacade.findByName(name);
    }

    @Override
    public Task findById(Integer id) {
        return taskFacade.find(id);
    }

    @Override
    public List<Task> findAll() {
        return taskFacade.findAll();
    }

    @Override
    public List<Task> findByPriority(Priority priority) {
        return taskFacade.findByPriority(priority);
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        return taskFacade.findByStatus(status);
    }

    @Override
    public List<Task> findExpired(Timestamp date) {
        return taskFacade.findExpired(date);
    }

    @Override
    public EntityManager getEntityManager() {
        return taskFacade.getEntityManager();
    }

    @Override
    public void edit(Task task) {
        taskFacade.edit(task);
    }
}
