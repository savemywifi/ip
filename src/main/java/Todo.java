import java.util.ArrayList;

class Todo extends Task {
    public Todo(String name) {
        super(name);
    }

    @Override
    protected String getSymbol() {
        return "T";
    }

    @Override
    protected ArrayList<String> toDataList() {
        return super.toDataList();
    }
}
