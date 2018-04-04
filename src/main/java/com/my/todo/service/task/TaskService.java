package com.my.todo.service.task;

import com.my.todo.model.Task;
import com.my.todo.util.Priority;
import com.my.todo.util.TaskStatus;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by marcin on 30.11.15.
 */
public interface TaskService {
    public void add(Task task);
    public void remove(Integer id);
    public void edit(Task task);
    public List<Task> findByName(String name);
    public Task findById(Integer id);
    public List<Task> findAll();
    public List<Task> findByPriority(Priority priority);
    public List<Task> findByStatus(TaskStatus status);
    public List<Task> findExpired(Timestamp date);

    EntityManager getEntityManager();
}
