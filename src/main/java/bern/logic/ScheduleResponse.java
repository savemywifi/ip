package bern.logic;

import java.util.List;

import bern.task.Task;
import bern.ui.ScheduleFrame;
import javafx.scene.Node;

/**
 * A response from Bern that contains a Task
 */
public class ScheduleResponse extends Response {
    private List<TaskResponse> taskResponses;

    private ScheduleResponse(List<TaskResponse> taskResponses) {
        this.taskResponses = taskResponses;
    }

    public static ScheduleResponse getScheduleResponse(List<Task> tasks) {
        return new ScheduleResponse(tasks.stream().map(TaskResponse::new).toList());
    }

    @Override
    public Node getResponseNode() {
        return new ScheduleFrame(taskResponses);
    }
}
