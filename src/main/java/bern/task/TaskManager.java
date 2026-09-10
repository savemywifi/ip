package bern.task;

import java.util.ArrayList;
import java.util.List;

/** Stores and manages the application's tasks. */
public class TaskManager {
    private static TaskManager instance;

    private final ArrayList<Task> tasks = new ArrayList<>();

    private TaskManager() {
    }

    /** Returns the singleton task manager instance. */
    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }

        return instance;
    }

    /** Returns whether at least one task is stored. */
    public boolean hasTasks() {
        return !tasks.isEmpty();
    }

    /** Returns the number of stored tasks. */
    public int size() {
        return tasks.size();
    }

    /** Returns a copy of the stored task list. */
    public List<Task> getTaskList() {
        return new ArrayList<>(tasks);
    }

    /** Loads tasks into the manager if it has not already been populated. */
    public void loadTaskList(List<Task> taskList) {
        if (hasTasks()) {
            // Reject operation if task list already has tasks in it
            // TODO: have better error messages
            return;
        }

        tasks.addAll(taskList);
    }

    /** Adds a task and returns the corresponding confirmation message. */
    public String addTask(Task task) {
        tasks.add(task);

        return "added: " + task;
    }

    /** Returns a formatted list of the current tasks. */
    public String listTasks() {
        if (tasks.isEmpty()) {
            return "You have no tasks.";
        }

        StringBuilder sb = new StringBuilder("Here are your current tasks:\n");

        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("\n%d. %s", i + 1, tasks.get(i)));
        }

        return sb.toString();
    }

    /**
     * Returns a formatted list of tasks, filtered by a given search token
     * @param searchToken The token that tasks will be matched to
     * @return A list of tasks which contain the given token
     */
    public String findTasks(String searchToken) {
        if (tasks.isEmpty()) {
            return "You have no tasks.";
        }

        boolean tasksAdded = false;

        StringBuilder sb = new StringBuilder("Here are matching tasks in your list:\n");

        for (int i = 0; i < tasks.size(); i++) {
            for (String word : tasks.get(i).getName().split(" ")) {
                if (word.equalsIgnoreCase(searchToken)) {
                    tasksAdded = true;
                    sb.append(String.format("\n%d. %s", i + 1, tasks.get(i)));
                    break;
                }
            }
        }

        if (!tasksAdded) {
            return "No tasks match the given search token.";
        }

        return sb.toString();
    }

    /**
     * Marks a task as complete
     *
     * @param i The task index
     * @return The message marking the task as completed
     */
    public String markTask(int i) {
        assert 1 <= i && i <= tasks.size();
        Task task = tasks.get(i - 1);
        if (task.isDone()) {
            return "The following task is already marked as done:\n" + task;
        }

        task.setDone(true);

        return "Nice! I've marked this task as done:\n" + task;
    }

    /** Marks the task at the one-based index as not done. */
    public String unmarkTask(int i) {
        assert 1 <= i && i <= tasks.size();
        Task task = tasks.get(i - 1);

        if (!task.isDone()) {
            return "The following task is already not marked as done: \n" + task;
        }

        task.setDone(false);

        return "OK, I've marked this task as not done yet:\n" + task;
    }

    /** Deletes the task at the one-based index. */
    public String deleteTask(int i) {
        assert 1 <= i && i <= tasks.size();
        Task task = tasks.get(i - 1);

        tasks.remove(i - 1);
        return "OK, I've removed this task:\n" + task;
    }
}
