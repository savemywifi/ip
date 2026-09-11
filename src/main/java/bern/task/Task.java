package bern.task;

import java.util.ArrayList;

import bern.datetime.DateTime;
/**
 * A task that is to be completed. Contains a String description and can be marked as done or not done.
 */
public abstract class Task {
    private boolean done;
    private String name;

    /**
     * Constructor for Task object
     * @param name The name of the task
     */
    Task(String name) {
        this.name = name;
        this.done = false;
    }

    public String getName() {
        return name;
    }

    /**
     * Checks if the task is done
     *
     * @return true if the task is done, false otherwise
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Marks the task to be done or not done
     *
     * @param isDone the state to set the task to
     */
    public void setDone(boolean isDone) {
        done = isDone;
    }

    /**
     * The string representation of the task.
     *
     * @return The task's display string; [X] means done and [ ] means incomplete.
     */
    @Override
    public String toString() {
        return String.format("[%s][%s] %s",
                getSymbol(),
                done ? "X" : " ",
                name
        );
    }

    /**
     * Returns the task fields in save-file order: task symbol, completion flag (1 or 0) and task name
     *
     * @return The task fields in save-file order.
     */
    public ArrayList<String> toDataList() {
        ArrayList<String> dataList = new ArrayList<>();
        dataList.add(getSymbol());
        dataList.add(isDone() ? "1" : "0");
        dataList.add(getName());
        return dataList;
    }

    /** Returns the one-letter symbol used when displaying this task. */
    protected abstract String getSymbol();

    /** Returns true if the task is associated with a specific day */
    protected abstract boolean isOnDay(DateTime dateTime);
}
