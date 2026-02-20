public class Event extends Task {
    private static final String DONE = "1";
    private static final String NOT_DONE = "0";

    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toStorageString() {
        String done = isDone() ? DONE : NOT_DONE;
        return "E | " + done + " | " + getDescription() + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}