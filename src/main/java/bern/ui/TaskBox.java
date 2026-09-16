package bern.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Represents a dialog box consisting of a task
 */
public class TaskBox extends VBox {
    private static final Image markImage = new Image(DialogBox.class.getResourceAsStream("/images/mark.png"));
    private static final Image unmarkImage = new Image(DialogBox.class.getResourceAsStream("/images/unmark.png"));

    @FXML
    protected ImageView mark;

    /**
     * Loads a task box's FXML layout using this instance as its root and controller.
     * Prints the stack trace if loading fails.
     *
     * @param resourcePath The absolute classpath location of the task box's layout.
     */
    protected void loadView(String resourcePath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource(resourcePath));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void setMark(boolean isDone) {
        mark.setImage(isDone ? markImage : unmarkImage);
    }
}
