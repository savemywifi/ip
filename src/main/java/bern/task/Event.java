package bern.task;

import java.util.ArrayList;

import bern.datetime.DateTime;

/** Represents a task that takes place between two date-time values. */
class Event extends Task {
    private final DateTime startDateTime;
    private final DateTime endDateTime;

    /** Creates an event task. */
    public Event(String name, DateTime startDateTime, DateTime endDateTime) {
        super(name);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Override
    protected String getSymbol() {
        return "E";
    }

    @Override
    public String toString() {
        return String.format("%s (from: %s to: %s)", super.toString(), startDateTime, endDateTime);
    }

    @Override
    public ArrayList<String> toDataList() {
        ArrayList<String> out = super.toDataList();
        out.add(startDateTime.toString());
        out.add(endDateTime.toString());
        return out;
    }
}
