package com.my.todo.model;

/**
 * Created by marcin on 05.01.16.
 */
public class GlobalContext {
    private static GlobalContext instance = null;

    private Task taskToEdit;

    private Task notifiedTask;

    private GlobalContext() {}

    public static GlobalContext getInstance() {
        if (instance == null) {
            instance = new GlobalContext();
        }
        return instance;
    }

    public Task getTaskToEdit() {
        return taskToEdit;
    }

    public void setTaskToEdit(Task taskToEdit) {
        this.taskToEdit = taskToEdit;
    }

    public Task getNotifiedTask() {
        return notifiedTask;
    }

    public void setNotifiedTask(Task notifiedTask) {
        this.notifiedTask = notifiedTask;
    }
}
