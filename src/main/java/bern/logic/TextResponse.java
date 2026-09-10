package bern.logic;

import bern.ui.DialogBox;
import javafx.scene.Node;

/**
 * A response from Bern that only contains text
 */
public class TextResponse extends Response {
    private String text;

    public TextResponse(String text) {
        this.text = text;
    }

    @Override
    public Node getResponseNode() {
        return DialogBox.getBernDialog(text);
    }

    @Override
    public String toString() {
        return text;
    }
}
