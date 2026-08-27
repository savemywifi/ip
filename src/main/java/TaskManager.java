import java.util.ArrayList;
import java.util.List;

class TaskManager {
    private static TaskManager instance;

    private final ArrayList<Task> tasks = new ArrayList<>();

    private TaskManager() {
    }

    static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }

        return instance;
    }

    boolean hasTasks() {
        return !tasks.isEmpty();
    }

    int size() {
        return tasks.size();
    }

    List<Task> getTaskList() {
        return new ArrayList<Task>(tasks);
    }

    void loadTaskList(List<Task> taskList) {
        if (hasTasks()) {
            // Reject operation if task list already has tasks in it
            // TODO: have better error messages
            return;
        }

        tasks.addAll(taskList);
    }

    String addTask(Task task) {
        tasks.add(task);

        return "> added: " + task;
    }

    String listTasks() {
        if (tasks.isEmpty()) {
            return "> You have no tasks.";
        }

        StringBuilder sb = new StringBuilder("> Here are your current tasks:\n");

        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("\n%d. %s", i + 1, tasks.get(i)));
        }

        return sb.toString();
    }

    String markTask(int i) {
        Task task = tasks.get(i - 1);

        if (task.isDone()) {
            return "The following task is already marked as done:\n" + task;
        }

        task.setDone(true);

        return "Nice! I've marked this task as done:\n" + task;
    }

    String unmarkTask(int i) {
        Task task = tasks.get(i - 1);

        if (!task.isDone()) {
            return "The following task is already not marked as done: \n" + task;
        }

        task.setDone(false);

        return "OK, I've marked this task as not done yet:\n" + task;
    }

    String deleteTask(int i) {
        Task task = tasks.get(i - 1);

        tasks.remove(i - 1);
        return "OK, I've removed this task:\n" + task;
    }
}
