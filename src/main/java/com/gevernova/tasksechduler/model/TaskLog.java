package com.gevernova.tasksechduler.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskLog {
    private final List<Task> executedTasks = new ArrayList<>();

    public void addLog(Task task) {
        executedTasks.add(task);
    }

    public List<Task> getAllLogs() {
        return executedTasks;
    }

    public List<Task> filterByName(String keyword) {
        return executedTasks.stream()
                .filter(t -> t.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}

