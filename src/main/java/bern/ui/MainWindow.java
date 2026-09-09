package bern.ui;

import bern.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Controller control;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaLog.png"));
    private Image bernImage = new Image(this.getClass().getResourceAsStream("/images/DaBern.png"));

    /**
     * Initializes the main view, displays the greeting, and loads saved tasks.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        /* Greet User */
        dialogContainer.getChildren().addAll(
                DialogBox.getBernDialog(Dialog.getInstance().greetUser(), bernImage)
        );

        /* Load Save Data */
        // TODO: figure out how to send the error messages
        if (control.loadTasks()) {
            dialogContainer.getChildren().add(
                    DialogBox.getBernDialog(Dialog.getInstance().printLoadedTasks(), bernImage)
            );
        }
    }

    /**
     * Injects the Controller instance.
     *
     * @param c The controller used to process user commands.
     */
    public void setController(Controller c) {
        control = c;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Bern's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }
        String response = control.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBernDialog(response, bernImage)
        );
        userInput.clear();
    }
}
