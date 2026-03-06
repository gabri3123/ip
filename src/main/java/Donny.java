/**
 * Main class of the Donny task manager application.
 * It coordinates the UI, parser, storage, and task list.
 */
public class Donny {
    private static final String FILE_PATH = "./data/donny.txt";

    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    /**
     * Creates a Donny application using the given file path.
     *
     * @param filePath path to the save file
     */
    public Donny(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = loadTasks();
    }

    /**
     * Entry point of the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        new Donny(FILE_PATH).run();
    }

    /**
     * Runs the main command loop until the user exits.
     */
    public void run() {
        ui.printWelcome();

        while (true) {
            String line = ui.readCommand();

            try {
                if (Parser.isExitCommand(line)) {
                    ui.printExitMessage();
                    break;
                }

                handleCommand(line);
            } catch (DonnyException e) {
                ui.printError("OOPS! " + e.getMessage());
            } catch (Exception e) {
                ui.printError("OOPS! Something went wrong. Please check your input format.");
            }
        }
    }

    /**
     * Loads tasks from storage. If loading fails, an empty task list is used instead.
     *
     * @return loaded task list, or an empty one if loading fails
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.load());
        } catch (DonnyException e) {
            ui.printError("OOPS! " + e.getMessage() + "\n Starting with an empty task list.");
            return new TaskList();
        }
    }

    /**
     * Handles a single user command by dispatching it to the relevant operation.
     *
     * @param line full user input
     * @throws DonnyException if the command is invalid
     */
    private void handleCommand(String line) throws DonnyException {
        String command = Parser.parseCommandWord(line);
        String args = Parser.parseArguments(line);

        switch (command) {
        case "list":
            ui.printList(tasks);
            break;
        case "mark":
            markTask(args);
            break;
        case "unmark":
            unmarkTask(args);
            break;
        case "delete":
            deleteTask(args);
            break;
        case "todo":
            addTodo(args);
            break;
        case "deadline":
            addDeadline(args);
            break;
        case "event":
            addEvent(args);
            break;
        case "find":
            findTask(args);
            break;
        default:
            throw new DonnyException(
                    "I don't understand that command. Try: list, todo, deadline, event, mark, unmark, find, bye");
        }
    }

    /**
     * Marks the specified task as done.
     *
     * @param args task number from the user input
     * @throws DonnyException if the index is invalid
     */
    private void markTask(String args) throws DonnyException {
        int index = Parser.parseIndex(args, tasks.size(), "mark");
        tasks.markTask(index);
        storage.save(tasks.getTasks());
        ui.printTaskStatusChange("Nice! I've marked this task as done:", tasks.get(index));
    }

    /**
     * Marks the specified task as not done.
     *
     * @param args task number from the user input
     * @throws DonnyException if the index is invalid
     */
    private void unmarkTask(String args) throws DonnyException {
        int index = Parser.parseIndex(args, tasks.size(), "unmark");
        tasks.unmarkTask(index);
        storage.save(tasks.getTasks());
        ui.printTaskStatusChange("OK, I've marked this task as not done yet:", tasks.get(index));
    }

    /**
     * Deletes the specified task from the task list.
     *
     * @param args task number from the user input
     * @throws DonnyException if the index is invalid
     */
    private void deleteTask(String args) throws DonnyException {
        int index = Parser.parseIndex(args, tasks.size(), "delete");
        Task removed = tasks.deleteTask(index);
        storage.save(tasks.getTasks());
        ui.printDeleted(removed, tasks.size());
    }

    /**
     * Adds a todo task to the task list.
     *
     * @param args description of the todo task
     * @throws DonnyException if the input is invalid
     */
    private void addTodo(String args) throws DonnyException {
        Task task = Parser.parseTodo(args);
        tasks.addTask(task);
        storage.save(tasks.getTasks());
        ui.printAdded(task, tasks.size());
    }

    /**
     * Adds a deadline task to the task list.
     *
     * @param args description and deadline details
     * @throws DonnyException if the input is invalid
     */
    private void addDeadline(String args) throws DonnyException {
        Task task = Parser.parseDeadline(args);
        tasks.addTask(task);
        storage.save(tasks.getTasks());
        ui.printAdded(task, tasks.size());
    }

    /**
     * Adds an event task to the task list.
     *
     * @param args description and event timing details
     * @throws DonnyException if the input is invalid
     */
    private void addEvent(String args) throws DonnyException {
        Task task = Parser.parseEvent(args);
        tasks.addTask(task);
        storage.save(tasks.getTasks());
        ui.printAdded(task, tasks.size());
    }

    /**
     * Finds tasks whose descriptions contain the given keyword.
     *
     * @param args keyword to search for
     * @throws DonnyException if the keyword is missing
     */
    private void findTask(String args) throws DonnyException {
        if (args.isEmpty()) {
            throw new DonnyException("Please provide a keyword to search.");
        }

        TaskList matches = tasks.find(args);
        ui.printFindResults(matches);
    }
}