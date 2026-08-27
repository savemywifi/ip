package bern.task;

import java.util.ArrayList;

/**
 * A task with no deadline. Contains only a String description.
 */
class Todo extends Task {
    /**
     * Constructor for a Todo
     *
     * @param name The name of the task
     */
    Todo(String name) {
        super(name);
    }

    /**
     * Returns a symbol that represents the Todo task type.
     *
     * @return The symbol representing the task type.
     */
    @Override
    protected String getSymbol() {
        return "T";
    }

    /**
     * Returns a list of String data used for saving the task
     *
     * @return A list of String data representing the Todo
     */
    @Override
    public ArrayList<String> toDataList() {
        return super.toDataList();
    }
}
