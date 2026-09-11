package bern.ui;

import java.io.IOException;

import bern.task.Deadline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

/**
 * Represents a task box consisting of a deadline.
 */
public class DeadlineBox extends TaskBox {
    @FXML
    private Label time;
    @FXML
    private Label description;

    /**
     * Create a task box with the given deadline.
     *
     * @param deadline The deadline represented by the task box.
     */
    public DeadlineBox(Deadline deadline) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DeadlineBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setMark(deadline.isDone());
        time.setText(deadline.getDateTimeString());
        description.setText(deadline.getName());
    }
}
