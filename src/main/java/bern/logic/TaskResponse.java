package bern.logic;

import java.util.List;

import bern.task.Deadline;
import bern.task.Event;
import bern.task.Task;
import bern.task.Todo;
import bern.ui.DeadlineBox;
import bern.ui.DialogBox;
import bern.ui.EventBox;
import bern.ui.TodoBox;
import javafx.scene.Node;

/**
 * A response from Bern that contains a Task
 */
public class TaskResponse extends Response {
    private String text;
    private Task task;

    /**
     * Creates a TaskResponse from a given task.
     *
     * @param task The task to be included in the response.
     */
    public TaskResponse(Task task) {
        this.task = task;
        this.text = "";
    }

    /**
     * Creates a TaskResponse with a given message
     *
     * @param task The task to be included in the response.
     * @param text The text to be included in the response.
     */
    public TaskResponse(Task task, String text) {
        this.task = task;
        this.text = text;
    }

    @Override
    public List<Node> getResponseNodes() {
        Node taskBox;
        if (task instanceof Todo) {
            taskBox = new TodoBox((Todo) task);
        } else if (task instanceof Deadline) {
            taskBox = new DeadlineBox((Deadline) task);
        } else if (task instanceof Event) {
            taskBox = new EventBox((Event) task);
        } else {
            throw new UnsupportedOperationException("Not yet implemented for this task type");
        }

        if (!this.text.isEmpty()) {
            DialogBox dialog = DialogBox.getBernDialog(text);
            dialog.add(taskBox);
            return List.of(dialog);
        }

        return List.of(taskBox);
    }
}
