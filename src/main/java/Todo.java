class Todo extends Task {
    public Todo(String name) {
        super(name);
    }

    @Override
    protected String getSymbol() {
        return "T";
    }
}
