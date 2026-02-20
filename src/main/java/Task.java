public abstract class Task {
    private final String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    protected String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Returns the line format used for saving into the data file. */
    public abstract String toStorageString();

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}