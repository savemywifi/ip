import java.util.ArrayList;

class Deadline extends Task {
    private final DateTime deadlineDateTime;

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
        return String.format("%s (by: %s)", super.toString(), this.deadlineDateTime);
    }

    @Override
    protected ArrayList<String> toDataList() {
        ArrayList<String> out = super.toDataList();
        out.add(deadlineDateTime.toString());
        return out;
    }
}
