import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final String SPLIT_REGEX = "\\s*\\|\\s*";
    private static final String DELIMITER = " | ";

    private static final String DONE = "1";
    private static final String NOT_DONE = "0";

    private static final String TYPE_TODO = "T";
    private static final String TYPE_DEADLINE = "D";
    private static final String TYPE_EVENT = "E";

    private static final int MIN_PARTS = 3;
    private static final int IDX_TYPE = 0;
    private static final int IDX_DONE = 1;
    private static final int IDX_DESC = 2;
    private static final int IDX_DEADLINE_BY = 3;
    private static final int IDX_EVENT_FROM = 3;
    private static final int IDX_EVENT_TO = 4;

    private final Path filePath;

    public Storage(String relativePath) {
        this.filePath = Paths.get(relativePath);
    }

    public ArrayList<Task> load() throws DonnyException {
        ensureStorageReady();

        List<String> lines = readAllLines();
        ArrayList<Task> tasks = new ArrayList<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            tasks.add(parseLine(line)); // fail fast on corrupted lines
        }

        return tasks;
    }

    public void save(ArrayList<Task> tasks) throws DonnyException {
        ensureStorageReady();

        ArrayList<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toStorageString());
        }

        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new DonnyException("Unable to write to save file: " + filePath);
        }
    }

    private List<String> readAllLines() throws DonnyException {
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new DonnyException("Unable to read save file: " + filePath);
        }
    }

    private void ensureStorageReady() throws DonnyException {
        try {
            Path parent = filePath.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new DonnyException("Unable to create data folder/file: " + filePath);
        }
    }

    private Task parseLine(String line) throws DonnyException {
        String[] parts = line.split(SPLIT_REGEX);
        if (parts.length < MIN_PARTS) {
            throw new DonnyException("Corrupted save line: " + line);
        }

        String type = parts[IDX_TYPE];
        boolean isDone = parseDoneFlag(parts[IDX_DONE], line);
        String desc = parts[IDX_DESC];

        Task task = createTask(type, desc, parts, line);
        setTaskDoneStatus(task, isDone);
        return task;
    }

    private boolean parseDoneFlag(String doneFlag, String originalLine) throws DonnyException {
        if (DONE.equals(doneFlag)) {
            return true;
        }
        if (NOT_DONE.equals(doneFlag)) {
            return false;
        }
        throw new DonnyException("Corrupted done flag in line: " + originalLine);
    }

    private Task createTask(String type, String desc, String[] parts, String originalLine) throws DonnyException {
        switch (type) {
        case TYPE_TODO:
            return new Todo(desc);
        case TYPE_DEADLINE:
            requireParts(parts, IDX_DEADLINE_BY + 1, "Corrupted deadline line: " + originalLine);
            return new Deadline(desc, parts[IDX_DEADLINE_BY]);
        case TYPE_EVENT:
            requireParts(parts, IDX_EVENT_TO + 1, "Corrupted event line: " + originalLine);
            return new Event(desc, parts[IDX_EVENT_FROM], parts[IDX_EVENT_TO]);
        default:
            throw new DonnyException("Unknown task type in line: " + originalLine);
        }
    }

    private void requireParts(String[] parts, int requiredLength, String errorMessage) throws DonnyException {
        if (parts.length < requiredLength) {
            throw new DonnyException(errorMessage);
        }
    }

    private void setTaskDoneStatus(Task task, boolean isDone) {
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
    }

    private String serializeTask(Task task) throws DonnyException {
        String done = task.isDone() ? DONE : NOT_DONE;

        if (task instanceof Todo) {
            return TYPE_TODO + DELIMITER + done + DELIMITER + task.getDescription();
        }

        if (task instanceof Deadline) {
            Deadline d = (Deadline) task;
            return TYPE_DEADLINE + DELIMITER + done + DELIMITER + d.getDescription()
                    + DELIMITER + d.getBy();
        }

        if (task instanceof Event) {
            Event e = (Event) task;
            return TYPE_EVENT + DELIMITER + done + DELIMITER + e.getDescription()
                    + DELIMITER + e.getFrom() + DELIMITER + e.getTo();
        }

        throw new DonnyException("Unknown task type, cannot save.");
    }
}