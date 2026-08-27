package bern.task;

import java.util.ArrayList;

/** Represents a task without a deadline or event time. */
class Todo extends Task {
    /** Creates a todo task. */
    public Todo(String name) {
        super(name);
    }

    @Override
    protected String getSymbol() {
        return "T";
    }

    @Override
    public ArrayList<String> toDataList() {
        return super.toDataList();
    }
}
