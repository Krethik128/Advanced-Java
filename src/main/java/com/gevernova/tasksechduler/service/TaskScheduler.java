package com.gevernova.tasksechduler.service;

import com.gevernova.tasksechduler.model.Task;
import com.gevernova.tasksechduler.model.TaskLog;

import java.time.LocalDateTime;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;

public class TaskScheduler {
    private final PriorityQueue<Task> queue = new PriorityQueue<>();
    private final TaskLog log = new TaskLog();

    public void scheduleTask(Task task) {
        queue.add(task);
    }

    public void runDueTasks() {
        LocalDateTime now = LocalDateTime.now();
        while (!queue.isEmpty() && queue.peek().getScheduledTime().isBefore(now.plusSeconds(1))) {
            Task task = queue.poll();
            task.run();
            log.addLog(task);
        }
    }

    public List<Task> getUpcomingTasks() {
        return new ArrayList<>(queue);
    }

    public TaskLog getLog() {
        return log;
    }
}

