public class Todo extends Task {
    private static final String DONE = "1";
    private static final String NOT_DONE = "0";

    public Todo(String description) {
        super(description);
    }

    @Override
    public String toStorageString() {
        String done = isDone() ? DONE : NOT_DONE;
        return "T | " + done + " | " + getDescription();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}