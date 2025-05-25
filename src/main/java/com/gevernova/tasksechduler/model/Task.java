package com.gevernova.tasksechduler.model;

import java.time.LocalDateTime;

public class Task implements Comparable<Task>, Runnable {
    private final String name;
    private final int priority;
    private final LocalDateTime scheduledTime;
    private final Runnable action;

    public Task(String name, int priority, LocalDateTime scheduledTime, Runnable action) {
        this.name = name;
        this.priority = priority;
        this.scheduledTime = scheduledTime;
        this.action = action;
    }

    public String getName() { return name; }
    public int getPriority() { return priority; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }

    @Override
    public void run() {
        action.run();
    }

    @Override
    public int compareTo(Task other) {
        int timeCompare = this.scheduledTime.compareTo(other.scheduledTime);
        return (timeCompare != 0) ? timeCompare : Integer.compare(other.priority, this.priority);
    }

    @Override
    public String toString() {
        return "Task{" + "name='" + name + '\'' + ", priority=" + priority + ", time=" + scheduledTime + '}';
    }
}

