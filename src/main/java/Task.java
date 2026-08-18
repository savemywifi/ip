class Task {
    private boolean done;
    private String name;
    /**
     * Constructor for Task object
     * @param name The name of the task
     */
    public Task(String name) {
        this.name = name;
        this.done = false;
    }

    /**
     * Checks if the task is done
     *
     * @return true if the task is done, false otherwise
     */
    public boolean isDone() {
        return this.done;
    }

    /**
     * Marks the task to be done or not done
     *
     * @param isDone the state to set the task to
     */
    public void setDone(boolean isDone) {
        this.done = isDone;
    }

    /**
     * The string representation of the task.
     *
     * @return The string representation of the task as described above. [X] indicates the task
     * is done, while [ ] indicates it is not done.
     */
    @Override
    public String toString() {
        return String.format("[%s] %s",
                done ? "X" : " ",
                name
        );
    }
}