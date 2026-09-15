package bern.task;

import java.util.ArrayList;
import java.util.List;

import bern.datetime.DateTime;
import bern.logic.Response;
import bern.logic.ScheduleResponse;
import bern.logic.TaskResponse;
import bern.ui.Dialog;

/** Stores and manages the application's tasks. */
public class TaskManager {
    private static TaskManager instance;
    private static Dialog dialog = Dialog.getInstance();

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
    public Response listTasks() {
        if (tasks.isEmpty()) {
            return dialog.printNoTasksError();
        }

        return ScheduleResponse.getScheduleResponse("Here are your tasks: ", tasks);
    }

    /**
     * Returns a formatted message containing tasks whose names include a case-insensitive whole-word match for the
     * search token.
     * @param searchToken The token that tasks will be matched to
     * @return A formatted message containing matching tasks, or a message indicating that no tasks match.
     */
    public Response findTasks(String searchToken) {
        if (tasks.isEmpty()) {
            return dialog.printNoTasksError();
        }

        ArrayList<Task> filteredTasks = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            for (String word : tasks.get(i).getName().split(" ")) {
                if (word.equalsIgnoreCase(searchToken)) {
                    filteredTasks.add(tasks.get(i));
                    break;
                }
            }
        }

        if (filteredTasks.isEmpty()) {
            return dialog.printMessage("No tasks match the given search token.");
        }

        return ScheduleResponse.getScheduleResponse("Here are matching tasks in your list:", filteredTasks);
    }

    /**
     * Displays schedule for a specific day
     *
     * @param dateTime The given date time
     * @return The schedule for that specific date
     */
    public Response showSchedule(DateTime dateTime) {
        ArrayList<Task> schedule = new ArrayList<>();

        for (Task task : tasks) {
            if (!task.isOnDay(dateTime)) {
                continue;
            }

            assert task instanceof IDateTimeComparable;

            schedule.add(task);
        }

        if (schedule.isEmpty()) {
            return dialog.printNoTasksError();
        }

        schedule.sort((task1, task2) -> (
                (IDateTimeComparable) task1).compareDateTime((IDateTimeComparable) task2));

        return ScheduleResponse.getScheduleResponse(
                "Here are the tasks for " + dateTime.toString(), schedule);
    }

    /**
     * Marks a task as complete
     *
     * @param i The one-based task index
     * @return The message marking the task as completed
     */
    public Response markTask(int i) {
        assert 1 <= i && i <= tasks.size();
        Task task = tasks.get(i - 1);
        if (task.isDone()) {
            return new TaskResponse(task, "The following task is already marked as done:");
        }

        task.setDone(true);

        return new TaskResponse(task, "Nice! I've marked this task as done:");
    }

    /**
     * Marks the task at the one-based index as not done.
     *
     * @param i The one-based task number.
     * @return The confirmation message for the updated task.
     */
    public Response unmarkTask(int i) {
        assert 1 <= i && i <= tasks.size();
        Task task = tasks.get(i - 1);

        if (!task.isDone()) {
            return new TaskResponse(task, "The following task is already marked as not done:");
        }

        task.setDone(false);

        return new TaskResponse(task, "OK, I've marked this task as not done yet:");
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
