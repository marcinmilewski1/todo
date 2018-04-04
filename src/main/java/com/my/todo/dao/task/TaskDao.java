package com.my.todo.dao.task;

import com.my.todo.dao.AbstractDao;
import com.my.todo.model.Task;
import com.my.todo.util.Priority;
import com.my.todo.util.TaskStatus;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by marcin on 30.11.15.
 */
public interface TaskDao extends AbstractDao<Task> {
    public List<Task> findByPriority(Priority priority);
    public List<Task> findByStatus(TaskStatus status);
    public List<Task> findExpired(Timestamp date);
    public List<Task> findByName(String name);
}
