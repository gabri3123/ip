import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading tasks from the storage file
 * and saving tasks back to the file.
 */
public class Storage {
    private static final String SPLIT_REGEX = "\\s*\\|\\s*";
    private static final String DONE = "1";
    private static final String NOT_DONE = "0";
    private static final int MIN_PARTS = 3;

    private final Path filePath;

    /**
     * Creates a Storage object using the given relative file path.
     *
     * @param relativePath path to storage file
     */
    public Storage(String relativePath) {
        this.filePath = Paths.get(relativePath);
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return loaded tasks
     * @throws DonnyException if reading fails
     */
    public ArrayList<Task> load() throws DonnyException {
        ensureStorageReady();

        List<String> lines = readAllLines();
        ArrayList<Task> tasks = new ArrayList<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            tasks.add(parseLine(line));
        }

        return tasks;
    }

    /**
     * Saves tasks into the storage file.
     *
     * @param tasks tasks to save
     * @throws DonnyException if writing fails
     */
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

    /**
     * Reads all lines from the storage file.
     *
     * @return lines in the file
     * @throws DonnyException if reading fails
     */
    private List<String> readAllLines() throws DonnyException {
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new DonnyException("Unable to read save file: " + filePath);
        }
    }

    /**
     * Ensures that the storage directory and file exist before reading or writing.
     *
     * @throws DonnyException if the file or folder cannot be created
     */
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

    /**
     * Parses one line from storage into a Task object.
     *
     * @param line one saved task line
     * @return parsed task
     * @throws DonnyException if the line format is invalid
     */
    private Task parseLine(String line) throws DonnyException {
        String[] parts = line.split(SPLIT_REGEX);
        if (parts.length < MIN_PARTS) {
            throw new DonnyException("Corrupted save line: " + line);
        }

        String type = parts[0];
        boolean isDone = parseDoneFlag(parts[1], line);
        String desc = parts[2];

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

    /**
     * Parses the done flag from storage.
     *
     * @param doneFlag saved done flag
     * @param originalLine original line for error reporting
     * @return true if task is done
     * @throws DonnyException if the flag is invalid
     */
    private boolean parseDoneFlag(String doneFlag, String originalLine) throws DonnyException {
        if (DONE.equals(doneFlag)) {
            return true;
        }
        if (NOT_DONE.equals(doneFlag)) {
            return false;
        }
        throw new DonnyException("Corrupted done flag in line: " + originalLine);
    }
}