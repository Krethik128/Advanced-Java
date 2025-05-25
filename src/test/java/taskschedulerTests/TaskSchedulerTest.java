package taskschedulerTests;

import com.gevernova.tasksechduler.service.TaskScheduler;
import com.gevernova.tasksechduler.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskSchedulerTest {

    private TaskScheduler scheduler;

    @BeforeEach
    public void setUp() {
        scheduler = new TaskScheduler();
    }

    @Test
    public void testTasksWithSameScheduledTimeDifferentPriority() {
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);
        Task lowPriority = new Task("Low", 1, now, () -> System.out.println("Low"));
        Task highPriority = new Task("High", 5, now, () -> System.out.println("High"));

        scheduler.scheduleTask(lowPriority);
        scheduler.scheduleTask(highPriority);
        scheduler.runDueTasks();

        List<Task> logs = scheduler.getLog().getAllLogs();
        assertEquals(2, logs.size());
        assertEquals("High", logs.get(0).getName(), "High priority should execute before low");
    }

    @Test
    public void testEmptySchedulerDoesNotFail() {
        assertDoesNotThrow(() -> scheduler.runDueTasks(), "Scheduler should not throw on empty run");
        assertTrue(scheduler.getLog().getAllLogs().isEmpty(), "No logs expected for empty scheduler");
    }

    @Test
    public void testLogFilteringByName() {
        Task task1 = new Task("Database Backup", 3, LocalDateTime.now().minusSeconds(5), () -> {});
        Task task2 = new Task("Email Job", 3, LocalDateTime.now().minusSeconds(4), () -> {});
        Task task3 = new Task("Backup Files", 3, LocalDateTime.now().minusSeconds(3), () -> {});

        scheduler.scheduleTask(task1);
        scheduler.scheduleTask(task2);
        scheduler.scheduleTask(task3);
        scheduler.runDueTasks();

        List<Task> filtered = scheduler.getLog().filterByName("backup");
        assertEquals(2, filtered.size(), "Two tasks should match 'backup'");
    }

    @Test
    public void testMultipleFutureTasksRemainInQueue() {
        Task future1 = new Task("Future A", 1, LocalDateTime.now().plusMinutes(2), () -> {});
        Task future2 = new Task("Future B", 2, LocalDateTime.now().plusMinutes(3), () -> {});
        scheduler.scheduleTask(future1);
        scheduler.scheduleTask(future2);

        scheduler.runDueTasks();
        assertEquals(2, scheduler.getUpcomingTasks().size(), "Future tasks should remain in queue");
    }

    @Test
    public void testImmediateAndFutureTaskExecution() {
        Task nowTask = new Task("Immediate", 1, LocalDateTime.now().minusSeconds(2), () -> {});
        Task futureTask = new Task("Later", 1, LocalDateTime.now().plusSeconds(60), () -> {});

        scheduler.scheduleTask(nowTask);
        scheduler.scheduleTask(futureTask);
        scheduler.runDueTasks();

        assertEquals(1, scheduler.getLog().getAllLogs().size());
        assertEquals("Immediate", scheduler.getLog().getAllLogs().get(0).getName());
    }

    @Test
    public void testRunTaskWithSameTimeSamePriorityOrderIsFIFO() {
        LocalDateTime sameTime = LocalDateTime.now().minusSeconds(10);

        Task task1 = new Task("Task1", 1, sameTime, () -> {});
        Task task2 = new Task("Task2", 1, sameTime, () -> {});

        scheduler.scheduleTask(task1);
        scheduler.scheduleTask(task2);
        scheduler.runDueTasks();

        List<Task> logs = scheduler.getLog().getAllLogs();
        assertEquals("Task1", logs.get(0).getName());
        assertEquals("Task2", logs.get(1).getName());
    }
}
