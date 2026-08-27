package bern.task;

import java.util.ArrayList;

import bern.datetime.DateTime;

/** Represents a task that must be completed by a specified date and time. */
class Deadline extends Task {
    private final DateTime deadlineDateTime;

    /** Creates a deadline task. */
    public Deadline(String name, DateTime deadlineDateTime) {
        super(name);
        this.deadlineDateTime = deadlineDateTime;
    }

    @Override
    protected String getSymbol() {
        return "D";
    }

    @Override
    public String toString() {
        return String.format("%s (by: %s)", super.toString(), deadlineDateTime);
    }

    @Override
    public ArrayList<String> toDataList() {
        ArrayList<String> out = super.toDataList();
        out.add(deadlineDateTime.toString());
        return out;
    }
}
