package bern.ui;

import java.io.IOException;

import bern.task.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

/**
 * Represents a task box consisting of a deadline.
 */
public class EventBox extends TaskBox {
    @FXML
    private Label startTime;
    @FXML
    private Label endTime;
    @FXML
    private Label description;

    /**
     * Create a task box with the given event.
     *
     * @param event The event represented by the task box.
     */
    public EventBox(Event event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/EventBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startTime.setText(event.getStartTimeString());
        endTime.setText(event.getEndTimeString());
        description.setText(event.getName());
    }
}
