package bern.ui;

import javafx.fxml.FXML;
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

    protected void setMark(boolean isDone) {
        mark.setImage(isDone ? markImage : unmarkImage);
    }
}
