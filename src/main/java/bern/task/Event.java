package bern.task;

import bern.datetime.DateTime;

import java.util.ArrayList;

class Event extends Task {
    private final DateTime startDateTime;
    private final DateTime endDateTime;

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
        return String.format("%s (from: %s to: %s)", super.toString(), this.startDateTime, this.endDateTime);
    }

    @Override
    public ArrayList<String> toDataList() {
        ArrayList<String> out = super.toDataList();
        out.add(startDateTime.toString());
        out.add(endDateTime.toString());
        return out;
    }
}
