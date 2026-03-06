/**
 * Parses user input into commands, arguments, and task objects.
 */
public class Parser {

    /**
     * Returns whether the given line is the exit command.
     *
     * @param line full user input
     * @return true if the user wants to exit
     */
    public static boolean isExitCommand(String line) {
        return line.equalsIgnoreCase("bye");
    }

    /**
     * Extracts the command word from the full user input.
     *
     * @param line full user input
     * @return command word in lowercase
     */
    public static String parseCommandWord(String line) {
        String[] split = line.trim().split("\\s+", 2);
        return split[0].toLowerCase();
    }

    /**
     * Extracts the argument portion of the full user input.
     *
     * @param line full user input
     * @return command arguments, or empty string if none
     */
    public static String parseArguments(String line) {
        String[] split = line.trim().split("\\s+", 2);
        return split.length > 1 ? split[1].trim() : "";
    }

    /**
     * Parses a 1-based task number from user input into a 0-based index.
     *
     * @param args task number input
     * @param size current task list size
     * @param commandWord command name for error messages
     * @return 0-based task index
     * @throws DonnyException if the index is invalid
     */
    public static int parseIndex(String args, int size, String commandWord) throws DonnyException {
        if (args.isEmpty()) {
            throw new DonnyException("Please specify a task number. Example: " + commandWord + " 2");
        }

        int num;
        try {
            num = Integer.parseInt(args.trim());
        } catch (NumberFormatException e) {
            throw new DonnyException("That task number isn't a valid integer. Example: " + commandWord + " 2");
        }

        int index = num - 1;
        if (index < 0 || index >= size) {
            throw new DonnyException("Task number out of range. Use: list (then pick 1 to " + size + ")");
        }
        return index;
    }

    /**
     * Parses a todo command into a Todo task.
     *
     * @param args todo description
     * @return parsed Todo task
     * @throws DonnyException if the description is missing
     */
    public static Task parseTodo(String args) throws DonnyException {
        if (args.isEmpty()) {
            throw new DonnyException("I need a description for a todo. Try: todo read book");
        }
        return new Todo(args);
    }

    /**
     * Parses a deadline command into a Deadline task.
     *
     * @param args deadline description and /by section
     * @return parsed Deadline task
     * @throws DonnyException if the format is invalid
     */
    public static Task parseDeadline(String args) throws DonnyException {
        if (args.isEmpty()) {
            throw new DonnyException(
                    "I need a description and /by for a deadline. Try: deadline return book /by Sunday");
        }

        String[] parts = args.split("\\s*/by\\s*", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new DonnyException("Deadline format should be: deadline <description> /by <time>");
        }

        return new Deadline(parts[0].trim(), parts[1].trim());
    }

    /**
     * Parses an event command into an Event task.
     *
     * @param args event description and timing details
     * @return parsed Event task
     * @throws DonnyException if the format is invalid
     */
    public static Task parseEvent(String args) throws DonnyException {
        if (args.isEmpty()) {
            throw new DonnyException(
                    "I need an event with /from and /to. Try: event meeting /from Mon 2pm /to Mon 4pm");
        }

        String[] first = args.split("\\s*/from\\s*", 2);
        if (first.length < 2 || first[0].trim().isEmpty()) {
            throw new DonnyException("Event format should be: event <description> /from <start> /to <end>");
        }
        String description = first[0].trim();

        String[] second = first[1].split("\\s*/to\\s*", 2);
        if (second.length < 2 || second[0].trim().isEmpty() || second[1].trim().isEmpty()) {
            throw new DonnyException("Event format should be: event <description> /from <start> /to <end>");
        }

        return new Event(description, second[0].trim(), second[1].trim());
    }
}