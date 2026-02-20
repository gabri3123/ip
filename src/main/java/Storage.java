import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage(String relativePath) {
        this.filePath = Paths.get(relativePath); // OS-independent
    }

    public ArrayList<Task> load() throws DonnyException {
        ensureFileExists();

        ArrayList<Task> tasks = new ArrayList<>();
        List<String> lines;

        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new DonnyException("Unable to read save file: " + filePath);
        }

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            try {
                tasks.add(parseLine(line));
            } catch (DonnyException e) {
                // corrupted line: skip it (stretch goal handling)
                // you can also choose to throw instead
            }
        }

        return tasks;
    }

    public void save(ArrayList<Task> tasks) throws DonnyException {
        ensureFileExists();

        ArrayList<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(serializeTask(task));
        }

        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new DonnyException("Unable to write to save file: " + filePath);
        }
    }

    private void ensureFileExists() throws DonnyException {
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
        // Formats:
        // T | 1 | read book
        // D | 0 | return book | Sunday
        // E | 0 | meeting | Mon 2pm | Mon 4pm

        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            throw new DonnyException("Corrupted save line: " + line);
        }

        String type = parts[0];
        String doneFlag = parts[1];
        String desc = parts[2];

        boolean isDone;
        if (doneFlag.equals("1")) {
            isDone = true;
        } else if (doneFlag.equals("0")) {
            isDone = false;
        } else {
            throw new DonnyException("Corrupted done flag in line: " + line);
        }

        Task task;
        switch (type) {
        case "T":
            task = new Todo(desc);
            break;
        case "D":
            if (parts.length < 4) {
                throw new DonnyException("Corrupted deadline line: " + line);
            }
            task = new Deadline(desc, parts[3]);
            break;
        case "E":
            if (parts.length < 5) {
                throw new DonnyException("Corrupted event line: " + line);
            }
            task = new Event(desc, parts[3], parts[4]);
            break;
        default:
            throw new DonnyException("Unknown task type in line: " + line);
        }

        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }

        return task;
    }

    private String serializeTask(Task task) throws DonnyException {
        String done = task.isDone() ? "1" : "0";

        if (task instanceof Todo) {
            return "T | " + done + " | " + task.getDescription();
        }

        if (task instanceof Deadline) {
            Deadline d = (Deadline) task;
            return "D | " + done + " | " + d.getDescription() + " | " + d.getBy();
        }

        if (task instanceof Event) {
            Event e = (Event) task;
            return "E | " + done + " | " + e.getDescription() + " | " + e.getFrom() + " | " + e.getTo();
        }

        throw new DonnyException("Unknown task type, cannot save.");
    }
}