package bern.logic;

import java.util.List;

import javafx.scene.Node;
/**
 * Encapsulates responses given by Controller in a format for MainWindow to parse
 */
public abstract class Response {
    protected String text;

    public abstract List<Node> getResponseNodes();
}
