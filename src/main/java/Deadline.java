public class Deadline extends Task {
    private static final String DONE = "1";
    private static final String NOT_DONE = "0";

    private final String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    public String getBy() {
        return by;
    }

    @Override
    public String toStorageString() {
        String done = isDone() ? DONE : NOT_DONE;
        return "D | " + done + " | " + getDescription() + " | " + by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}