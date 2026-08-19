class Deadline extends Task {
    private String deadlineDateTime;

    public Deadline(String name, String deadlineDateTime) {
        super(name);
        this.deadlineDateTime = deadlineDateTime;
    }

    @Override
    protected String getSymbol() {
        return "D";
    }

    @Override
    public String toString() {
        return String.format("%s (by: %s)", super.toString(), this.deadlineDateTime);
    }
}
