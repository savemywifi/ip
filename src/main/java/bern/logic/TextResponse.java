package bern.logic;

import java.util.List;

import bern.ui.DialogBox;
import javafx.scene.Node;

/**
 * A response from Bern that only contains text
 */
public class TextResponse extends Response {
    public TextResponse(String text) {
        this.text = text;
    }

    @Override
    public List<Node> getResponseNodes() {
        return List.of(DialogBox.getBernDialog(text));
    }

    @Override
    public String toString() {
        return text;
    }
}
