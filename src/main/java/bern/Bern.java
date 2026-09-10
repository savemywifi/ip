package bern;

import java.io.IOException;

import bern.ui.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Duke using FXML.
 */
public class Bern extends Application {
    private final Controller control = new Controller();
    private Image bernImage = new Image(this.getClass().getResourceAsStream("/images/DaBern.png"));

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Bern.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.getIcons().add(bernImage);
            stage.setTitle("Bern Tokens Task Manager");
            stage.setMinHeight(220);
            stage.setMinWidth(417);

            assert fxmlLoader.getController() != null;
            fxmlLoader.<MainWindow>getController().setController(control); // Inject the Controller instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
