package bern.logic;

import bern.task.Deadline;
import bern.task.Event;
import bern.task.Task;
import bern.ui.DeadlineBox;
import bern.ui.EventBox;
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
    public Node getResponseNode() {
        if (task instanceof Deadline) {
            return new DeadlineBox((Deadline) task);
        } else if (task instanceof Event) {
            return new EventBox((Event) task);
        } else {
            throw new UnsupportedOperationException("Not yet implemented for this task type");
        }
    }
}
