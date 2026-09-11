package bern.task;

import java.util.ArrayList;

import bern.datetime.DateTime;

/**
 * A task with a deadline. Contains a String description and a DateTime deadline.
 */
public class Deadline extends Task implements IDateTimeComparable {
    private final DateTime deadlineDateTime;

    /**
     * Constructor for a Deadline
     *
     * @param name The name of the task
     * @param deadlineDateTime The date (and time, if any) the task is due
     */
    Deadline(String name, DateTime deadlineDateTime) {
        super(name);
        this.deadlineDateTime = deadlineDateTime;
    }

    /**
     * Returns a symbol that represents the Deadline task type.
     *
     * @return The symbol representing the task type.
     */
    @Override
    protected String getSymbol() {
        return "D";
    }

    public String getDateTimeString() {
        return deadlineDateTime.toString();
    }

    /**
     * A string representation of a Deadline, in the form [task name] (by: [deadline])
     *
     * @return A string representation of a Deadline.
     */
    @Override
    public String toString() {
        return String.format("%s (by: %s)", super.toString(), deadlineDateTime);
    }

    /**
     * Returns a list of String data used for saving the task
     *
     * @return A list of String data representing the Deadline
     */
    @Override
    public ArrayList<String> toDataList() {
        ArrayList<String> out = super.toDataList();
        out.add(deadlineDateTime.toString());
        return out;
    }

    @Override
    protected boolean isOnDay(DateTime dateTime) {
        return deadlineDateTime.compareDate(dateTime) == 0;
    }

    @Override
    public DateTime getReferenceDateTime() {
        return deadlineDateTime;
    }

    @Override
    public int compareDateTime(IDateTimeComparable timeComparable) {
        return getReferenceDateTime().compareTo(timeComparable.getReferenceDateTime());
    }
}
