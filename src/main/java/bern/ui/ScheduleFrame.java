package bern.ui;

import java.io.IOException;
import java.util.List;

import bern.logic.Response;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class ScheduleFrame extends AnchorPane {
    @FXML
    private VBox taskContainer;

    /**
     * A constructor for ScheduleFrame
     *
     * @param responses The list of responses to include in the schedule
     */
    public ScheduleFrame(List<? extends Response> responses) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/ScheduleFrame.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObservableList<Node> scheduleItems = taskContainer.getChildren();
        for (Response r : responses) {
            scheduleItems.addAll(r.getResponseNodes());
        }
    }
}
