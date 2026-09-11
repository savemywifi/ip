package bern.logic;

import java.util.List;

import bern.task.Task;
import bern.ui.DialogBox;
import bern.ui.ScheduleFrame;
import javafx.scene.Node;

/**
 * A response from Bern that contains a list of Task responses
 */
public class ScheduleResponse extends Response {
    private List<TaskResponse> taskResponses;

    private ScheduleResponse(String text, List<TaskResponse> taskResponses) {
        this.text = text;
        this.taskResponses = taskResponses;
    }

    public static ScheduleResponse getScheduleResponse(String text, List<Task> tasks) {
        return new ScheduleResponse(text, tasks.stream().map(TaskResponse::new).toList());
    }

    @Override
    public List<Node> getResponseNodes() {
        return List.of(
                DialogBox.getBernDialog(text),
                new ScheduleFrame(taskResponses)
        );
    }
}
