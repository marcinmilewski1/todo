package com.my.todo.dao.task;

import com.my.todo.dao.AbstractDaoImpl;
import com.my.todo.model.Task;
import com.my.todo.util.Priority;
import com.my.todo.util.TaskStatus;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by marcin on 30.11.15.
 */
public class TaskDaoImpl extends AbstractDaoImpl<Task> implements TaskDao {
    public TaskDaoImpl() {
        super(Task.class);
    }

    @Override
    public List<Task> findByPriority(Priority priority) {
        TypedQuery<Task> query =
                getEntityManager().createNamedQuery("Task.findByPriority", Task.class);
        query.setParameter("priority", priority);
        List<Task> results = query.getResultList();
        return results;
    }


    @Override
    public List<Task> findByStatus(TaskStatus status) {
        TypedQuery<Task> query =
                getEntityManager().createNamedQuery("Task.findByStatus", Task.class);
        query.setParameter("status", status);
        List<Task> results = query.getResultList();
        return results;
    }

    @Override
    public List<Task> findExpired(Timestamp date) {
        TypedQuery<Task> query =
                getEntityManager().createNamedQuery("Task.findExpired", Task.class);
        query.setParameter("date", date);
        query.setParameter("taskStatus", TaskStatus.DONE);
        List<Task> results = query.getResultList();
        return results;
    }

    @Override
    public List<Task> findByName(String name) {
        TypedQuery<Task> query =
                getEntityManager().createNamedQuery("Task.findByName", Task.class);
        query.setParameter("name", name);
        List<Task> results = query.getResultList();
        return results;
    }

    @Override
    public EntityManager getEntityManager() {
        return super.getEntityManager();
    }
}
