package com.my.todo.model;

/**
 * Created by marcin on 05.01.16.
 */
public class GlobalPropertiesSingleton {
    private boolean timerStarted = false;
    private static GlobalPropertiesSingleton instance = null;

    private GlobalPropertiesSingleton() {
    }

    public static GlobalPropertiesSingleton getInstance() {
        if (instance == null) {
            instance = new GlobalPropertiesSingleton();
        }
        return instance;
    }

    public boolean isTimerStarted() {
        return timerStarted;
    }

    public void setTimerStarted(boolean timerStarted) {
        this.timerStarted = timerStarted;
    }
}
