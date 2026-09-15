package bern.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    private static final Image userImage = new Image(DialogBox.class.getResourceAsStream("/images/DaLog.png"));
    private static final Image bernImage = new Image(DialogBox.class.getResourceAsStream("/images/DaBern.png"));

    @FXML
    private VBox addons;
    @FXML
    private Label dialog;
    @FXML
    private ImageView bernDisplayPicture;
    @FXML
    private ImageView userDisplayPicture;

    private DialogBox(String text, Image img, boolean isUser) {
        assert userImage != null;
        assert bernImage != null;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ImageView displayPicture = isUser ? userDisplayPicture : bernDisplayPicture;
        ImageView nonDisplayPicture = isUser ? bernDisplayPicture : userDisplayPicture;

        dialog.setText(text);
        displayPicture.setImage(img);
        nonDisplayPicture.setOpacity(0d);
    }

    public static DialogBox getUserDialog(String text) {
        return new DialogBox(text, userImage, true);
    }

    public static DialogBox getBernDialog(String text) {
        return new DialogBox(text, bernImage, false);
    }

    /**
     * Adds a node to the dialog box.
     *
     * @param n The node to be added.
     */
    public void add(Node n) {
        addons.getChildren().add(n);
    }
}
