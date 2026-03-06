/**
 * Represents a task that must be completed by a deadline.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a deadline task.
     *
     * @param description task description
     * @param by deadline of the task
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline of the task.
     *
     * @return deadline string
     */
    public String getBy() {
        return by;
    }

    /**
     * Returns the save-file representation of this deadline task.
     *
     * @return storage string for deadline
     */
    @Override
    public String toStorageString() {
        String done = isDone() ? "1" : "0";
        return "D | " + done + " | " + getDescription() + " | " + by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}