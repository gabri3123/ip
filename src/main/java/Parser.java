public class Parser {

    public static boolean isExitCommand(String line) {
        return line.equalsIgnoreCase("bye");
    }

    public static String parseCommandWord(String line) {
        String[] split = line.trim().split("\\s+", 2);
        return split[0].toLowerCase();
    }

    public static String parseArguments(String line) {
        String[] split = line.trim().split("\\s+", 2);
        return split.length > 1 ? split[1].trim() : "";
    }

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

    public static Task parseTodo(String args) throws DonnyException {
        if (args.isEmpty()) {
            throw new DonnyException("I need a description for a todo. Try: todo read book");
        }
        return new Todo(args);
    }

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