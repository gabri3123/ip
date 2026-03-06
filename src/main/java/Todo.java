/**
 * Represents a todo task without any date or time information.
 */
public class Todo extends Task {

    /**
     * Creates a todo task with the given description.
     *
     * @param description todo description
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the save-file representation of this todo task.
     *
     * @return storage string for todo
     */
    @Override
    public String toStorageString() {
        String done = isDone() ? "1" : "0";
        return "T | " + done + " | " + getDescription();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}