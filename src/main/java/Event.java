import java.util.ArrayList;

class Event extends Task {
    private String startDateTime;
    private String endDateTime;

    public Event(String name, String startDateTime, String endDateTime) {
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
    protected ArrayList<String> toDataList() {
        ArrayList<String> out = super.toDataList();
        out.add(startDateTime);
        out.add(endDateTime);
        return out;
    }
}
