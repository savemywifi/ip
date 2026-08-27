package bern.task;

import bern.datetime.DateTime;

import java.util.ArrayList;

/**
 * A task that is an event. Contains a String description and two DateTimes signifying the start and end of the event.
 */
class Event extends Task {
    private final DateTime startDateTime;
    private final DateTime endDateTime;

    /**
     * Constructor for an Event
     *
     * @param name The name of the task
     * @param startDateTime The date (and time, if any) the event starts
     * @param endDateTime The date (and time, if any) the event ends
     */
    Event(String name, DateTime startDateTime, DateTime endDateTime) {
        super(name);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * Returns a symbol that represents the Event task type.
     *
     * @return The symbol representing the task type.
     */
    @Override
    protected String getSymbol() {
        return "E";
    }

    /**
     * A string representation of an Event, in the form [task name] (from: [start] to: [end])
     *
     * @return A string representation of a Deadline.
     */
    @Override
    public String toString() {
        return String.format("%s (from: %s to: %s)", super.toString(), this.startDateTime, this.endDateTime);
    }

    /**
     * Returns a list of String data used for saving the task
     *
     * @return A list of String data representing the Event
     */
    @Override
    public ArrayList<String> toDataList() {
        ArrayList<String> out = super.toDataList();
        out.add(startDateTime.toString());
        out.add(endDateTime.toString());
        return out;
    }
}
