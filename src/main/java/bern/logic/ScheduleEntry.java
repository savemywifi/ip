package bern.logic;

import bern.task.Task;

/** Stores an event's interval within one day and the timetable column assigned to it. */
public final class ScheduleEntry {
    private final Task task;
    private final int startMinute;
    private final int endMinute;
    private final int column;

    /**
     * Creates a timetable placement whose end is exclusive.
     *
     * @param task The original task represented by this placement.
     * @param startMinute The inclusive start, in minutes after midnight.
     * @param endMinute The exclusive end, at most 1440 for the end of the day.
     * @param column The zero-based display column.
     */
    public ScheduleEntry(Task task, int startMinute, int endMinute, int column) {
        this.task = task;
        this.startMinute = startMinute;
        this.endMinute = endMinute;
        this.column = column;
    }

    public Task getTask() {
        return task;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public int getColumn() {
        return column;
    }
}
