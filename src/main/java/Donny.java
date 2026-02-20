import java.util.ArrayList;
import java.util.Scanner;

public class Donny {
    private static final String LONG_LINE = "_________________________________\n";
    private static final String FILE_PATH = "./data/donny.txt";

    public static void main(String[] args) {
        printWelcome();

        Scanner in = new Scanner(System.in);
        Storage storage = new Storage(FILE_PATH);
        ArrayList<Task> tasks = loadTasks(storage);

        runCommandLoop(in, storage, tasks);
    }

    private static void printWelcome() {
        String logo = " ____   ___   _   _   _   _   _  _ \n"
                + "|  _ \\ / _ \\ | \\ | | | \\ | | | \\/ |\n"
                + "| | | | | | ||  \\| | |  \\| | \\   /\n"
                + "| |_| | |_| || |\\  | | |\\  |  | | \n"
                + "|____/ \\___/ |_| \\_| |_| \\_|  |_|  \n";

        System.out.println("Hello! I'm \n" + logo + "\n" + "What can I do for you? \n");
    }

    private static ArrayList<Task> loadTasks(Storage storage) {
        try {
            return storage.load();
        } catch (DonnyException e) {
            printError("OOPS! " + e.getMessage()
                    + "\n Starting with an empty task list.");
            return new ArrayList<>();
        }
    }

    private static void runCommandLoop(Scanner in, Storage storage, ArrayList<Task> tasks) {
        while (true) {
            String line = in.nextLine();

            try {
                if (shouldExit(line)) {
                    printExitMessage();
                    break;
                }

                handleCommand(line, storage, tasks);
            } catch (DonnyException e) {
                printError("OOPS! " + e.getMessage());
            } catch (Exception e) {
                printError("OOPS! Something went wrong. Please check your input format.");
            }
        }
    }

    private static boolean shouldExit(String line) {
        return line.equalsIgnoreCase("bye");
    }

    private static void printExitMessage() {
        System.out.println(LONG_LINE + "See you again, bye!\n" + LONG_LINE);
    }

    private static void handleCommand(String line, Storage storage, ArrayList<Task> tasks) throws DonnyException {
        String[] split = line.trim().split("\\s+", 2);
        String command = split[0].toLowerCase();
        String args = split.length > 1 ? split[1].trim() : "";

        switch (command) {
        case "list":
            printList(tasks);
            break;
        case "mark":
            markTask(args, tasks, storage);
            break;
        case "unmark":
            unmarkTask(args, tasks, storage);
            break;
        case "delete":
            deleteTask(args, tasks, storage);
            break;
        case "todo":
            addTodo(args, tasks, storage);
            break;
        case "deadline":
            addDeadline(args, tasks, storage);
            break;
        case "event":
            addEvent(args, tasks, storage);
            break;
        default:
            throw new DonnyException("I don't understand that command. Try: list, todo, deadline, event, mark, unmark, bye");
        }
    }

    private static void markTask(String args, ArrayList<Task> tasks, Storage storage) throws DonnyException {
        int index = parseIndex(args, tasks.size(), "mark");
        tasks.get(index).markAsDone();
        storage.save(tasks);
        printTaskStatusChange("Nice! I've marked this task as done:", tasks.get(index));
    }

    private static void unmarkTask(String args, ArrayList<Task> tasks, Storage storage) throws DonnyException {
        int index = parseIndex(args, tasks.size(), "unmark");
        tasks.get(index).markAsNotDone();
        storage.save(tasks);
        printTaskStatusChange("OK, I've marked this task as not done yet:", tasks.get(index));
    }

    private static void deleteTask(String args, ArrayList<Task> tasks, Storage storage) throws DonnyException {
        int index = parseIndex(args, tasks.size(), "delete");
        Task removed = tasks.remove(index);
        storage.save(tasks);

        System.out.println(LONG_LINE);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + removed);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        System.out.println(LONG_LINE);
    }

    private static void addTodo(String args, ArrayList<Task> tasks, Storage storage) throws DonnyException {
        if (args.isEmpty()) {
            throw new DonnyException("I need a description for a todo. Try: todo read book");
        }
        tasks.add(new Todo(args));
        storage.save(tasks);
        printAdded(tasks.get(tasks.size() - 1), tasks.size());
    }

    private static void addDeadline(String args, ArrayList<Task> tasks, Storage storage) throws DonnyException {
        if (args.isEmpty()) {
            throw new DonnyException("I need a description and /by for a deadline. Try: deadline return book /by Sunday");
        }

        String[] parts = args.split("\\s*/by\\s*", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new DonnyException("Deadline format should be: deadline <description> /by <time>");
        }

        tasks.add(new Deadline(parts[0].trim(), parts[1].trim()));
        storage.save(tasks);
        printAdded(tasks.get(tasks.size() - 1), tasks.size());
    }

    private static void addEvent(String args, ArrayList<Task> tasks, Storage storage) throws DonnyException {
        if (args.isEmpty()) {
            throw new DonnyException("I need an event with /from and /to. Try: event meeting /from Mon 2pm /to Mon 4pm");
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

        tasks.add(new Event(description, second[0].trim(), second[1].trim()));
        storage.save(tasks);
        printAdded(tasks.get(tasks.size() - 1), tasks.size());
    }

    private static void printTaskStatusChange(String header, Task task) {
        System.out.println(LONG_LINE);
        System.out.println(" " + header + "\n");
        System.out.println("  " + task);
        System.out.println(LONG_LINE);
    }

    private static void printError(String message) {
        System.out.println(LONG_LINE);
        System.out.println(" " + message);
        System.out.println(LONG_LINE);
    }

    private static void printList(ArrayList<Task> tasks) {
        System.out.println(LONG_LINE);
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
        System.out.println(LONG_LINE);
    }

    private static void printAdded(Task task, int size) {
        System.out.println(LONG_LINE);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + size + " tasks in the list.");
        System.out.println(LONG_LINE);
    }

    // args should be something like "2"
    private static int parseIndex(String args, int size, String commandWord) throws DonnyException {
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
}