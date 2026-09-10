package bern.task;

import java.util.ArrayList;
import java.util.List;

/** Stores and manages the application's tasks. */
public class TaskManager {
    private static TaskManager instance;

    private final ArrayList<Task> tasks = new ArrayList<>();

    private TaskManager() {
    }

    /**
     * Returns the singleton task manager instance.
     *
     * @return The singleton task manager instance.
     */
    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }

        return instance;
    }

    /**
     * Returns whether at least one task is stored.
     *
     * @return {@code true} if at least one task is stored; {@code false} otherwise.
     */
    public boolean hasTasks() {
        return !tasks.isEmpty();
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return The number of stored tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a shallow copy of the stored task list.
     *
     * @return A shallow copy of the stored task list.
     */
    public List<Task> getTaskList() {
        return new ArrayList<>(tasks);
    }

    /**
     * Loads tasks into the manager if it has not already been populated.
     *
     * @param taskList The tasks to load.
     * @return {@code true} if at least one task was loaded; {@code false} if the manager was already populated or
     *         the list was empty.
     */
    public boolean loadTaskList(List<Task> taskList) {
        if (hasTasks()) {
            // Reject operation if task list already has tasks in it
            // TODO: have better error messages
            return false;
        }

        tasks.addAll(taskList);

        return !tasks.isEmpty();
    }

    /**
     * Adds a task and returns the corresponding confirmation message.
     *
     * @param task The task to add.
     * @return The confirmation message for the added task.
     */
    public String addTask(Task task) {
        tasks.add(task);

        return "added: " + task;
    }

    /**
     * Returns a formatted list of the current tasks.
     *
     * @return A formatted message containing the current tasks.
     */
    public String listTasks() {
        if (tasks.isEmpty()) {
            return "You have no tasks.";
        }

        StringBuilder stringBuilder = new StringBuilder("Here are your current tasks:\n");

        for (int i = 0; i < tasks.size(); i++) {
            stringBuilder.append(String.format("\n%d. %s", i + 1, tasks.get(i)));
        }

        return stringBuilder.toString();
    }

    /**
     * Returns a formatted message containing tasks whose names include a case-insensitive whole-word match for the
     * search token.
     * @param searchToken The token that tasks will be matched to
     * @return A formatted message containing matching tasks, or a message indicating that no tasks match.
     */
    public String findTasks(String searchToken) {
        if (tasks.isEmpty()) {
            return "You have no tasks.";
        }

        boolean tasksAdded = false;

        StringBuilder stringBuilder = new StringBuilder("Here are matching tasks in your list:\n");

        for (int i = 0; i < tasks.size(); i++) {
            for (String word : tasks.get(i).getName().split(" ")) {
                if (word.equalsIgnoreCase(searchToken)) {
                    tasksAdded = true;
                    stringBuilder.append(String.format("\n%d. %s", i + 1, tasks.get(i)));
                    break;
                }
            }
        }

        if (!tasksAdded) {
            return "No tasks match the given search token.";
        }

        return stringBuilder.toString();
    }

    /**
     * Marks a task as complete
     *
     * @param i The one-based task index
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

    /**
     * Marks the task at the one-based index as not done.
     *
     * @param i The one-based task number.
     * @return The confirmation message for the updated task.
     */
    public String unmarkTask(int i) {
        assert 1 <= i && i <= tasks.size();
        Task task = tasks.get(i - 1);

        if (!task.isDone()) {
            return "The following task is already not marked as done: \n" + task;
        }

        task.setDone(false);

        return "OK, I've marked this task as not done yet:\n" + task;
    }

    /**
     * Deletes the task at the one-based index.
     *
     * @param i The one-based task number.
     * @return The confirmation message for the deleted task.
     */
    public String deleteTask(int i) {
        assert 1 <= i && i <= tasks.size();
        Task task = tasks.get(i - 1);

        tasks.remove(i - 1);
        return "OK, I've removed this task:\n" + task;
    }
}
