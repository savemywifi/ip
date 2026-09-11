package bern.ui;

import java.io.IOException;

import bern.task.Todo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

/**
 * Represents a task box consisting of a todo.
 */
public class TodoBox extends TaskBox {
    @FXML
    private Label description;

    /**
     * Create a task box with the given todo.
     *
     * @param todo The todo represented by the task box.
     */
    public TodoBox(Todo todo) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/TodoBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setMark(todo.isDone());
        description.setText(todo.getName());
    }
}
