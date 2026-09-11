package bern.task;

import bern.datetime.DateTime;

/**
 * A task with no deadline
 */
public class Todo extends Task {
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

    @Override
    protected boolean isOnDay(DateTime dateTime) {
        return false;
    }
}
