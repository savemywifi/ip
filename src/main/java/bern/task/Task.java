package bern.task;

import java.util.ArrayList;

/** Represents a task that can be completed and persisted. */
public abstract class Task {
    private boolean done;
    private String name;

    /**
     * Creates an incomplete task with the given name.
     *
     * @param name The name of the task.
     */
    public Task(String name) {
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
     * The string list representation of the task in the save file.
     *
     * @return The string list representation of the task as described above.
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
}
