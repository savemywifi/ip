package bern;

import java.io.IOException;

import bern.storage.SaveDataController;
import bern.task.TaskManager;
import bern.ui.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Duke using FXML.
 */
public class Bern extends Application {
    private final Controller control = new Controller();
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Bern.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setController(control); // Inject the Controller instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
