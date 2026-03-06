/**
 * Represents a task that happens over a time period.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task.
     *
     * @param description event description
     * @param from event start
     * @param to event end
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event start time.
     *
     * @return start time string
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the event end time.
     *
     * @return end time string
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns the save-file representation of this event task.
     *
     * @return storage string for event
     */
    @Override
    public String toStorageString() {
        String done = isDone() ? "1" : "0";
        return "E | " + done + " | " + getDescription() + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}