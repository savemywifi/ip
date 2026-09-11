package bern.logic;

import java.util.List;

import bern.task.Deadline;
import bern.task.Event;
import bern.task.Task;
import bern.task.Todo;
import bern.ui.DeadlineBox;
import bern.ui.EventBox;
import bern.ui.TodoBox;
import javafx.scene.Node;

/**
 * A response from Bern that contains a Task
 */
class TaskResponse extends Response {
    private Task task;

    public TaskResponse(Task task) {
        this.task = task;
    }

    @Override
    public List<Node> getResponseNodes() {
        if (task instanceof Todo) {
            return List.of(new TodoBox((Todo) task));
        } else if (task instanceof Deadline) {
            return List.of(new DeadlineBox((Deadline) task));
        } else if (task instanceof Event) {
            return List.of(new EventBox((Event) task));
        } else {
            throw new UnsupportedOperationException("Not yet implemented for this task type");
        }
    }
}
