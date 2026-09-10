package bern.logic;

import javafx.scene.Node;

/**
 * Encapsulates responses given by Controller in a format for MainWindow to parse
 */
public abstract class Response {
    public abstract Node getResponseNode();
}
