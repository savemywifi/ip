package bern.task;

import java.util.ArrayList;

class Todo extends Task {
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
