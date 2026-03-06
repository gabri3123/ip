public class Donny {
    private static final String FILE_PATH = "./data/donny.txt";

    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    public Donny(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = loadTasks();
    }

    public static void main(String[] args) {
        new Donny(FILE_PATH).run();
    }

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

    private TaskList loadTasks() {
        try {
            return new TaskList(storage.load());
        } catch (DonnyException e) {
            ui.printError("OOPS! " + e.getMessage() + "\n Starting with an empty task list.");
            return new TaskList();
        }
    }

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
                    "I don't understand that command. Try: list, todo, deadline, event, mark, unmark, bye");
        }
    }

    private void markTask(String args) throws DonnyException {
        int index = Parser.parseIndex(args, tasks.size(), "mark");
        tasks.markTask(index);
        storage.save(tasks.getTasks());
        ui.printTaskStatusChange("Nice! I've marked this task as done:", tasks.get(index));
    }

    private void unmarkTask(String args) throws DonnyException {
        int index = Parser.parseIndex(args, tasks.size(), "unmark");
        tasks.unmarkTask(index);
        storage.save(tasks.getTasks());
        ui.printTaskStatusChange("OK, I've marked this task as not done yet:", tasks.get(index));
    }

    private void deleteTask(String args) throws DonnyException {
        int index = Parser.parseIndex(args, tasks.size(), "delete");
        Task removed = tasks.deleteTask(index);
        storage.save(tasks.getTasks());
        ui.printDeleted(removed, tasks.size());
    }

    private void addTodo(String args) throws DonnyException {
        Task task = Parser.parseTodo(args);
        tasks.addTask(task);
        storage.save(tasks.getTasks());
        ui.printAdded(task, tasks.size());
    }

    private void addDeadline(String args) throws DonnyException {
        Task task = Parser.parseDeadline(args);
        tasks.addTask(task);
        storage.save(tasks.getTasks());
        ui.printAdded(task, tasks.size());
    }

    private void addEvent(String args) throws DonnyException {
        Task task = Parser.parseEvent(args);
        tasks.addTask(task);
        storage.save(tasks.getTasks());
        ui.printAdded(task, tasks.size());
    }

    private void findTask(String args) throws DonnyException {
        if (args.isEmpty()) {
            throw new DonnyException("Please provide a keyword to search.");
        }

        TaskList matches = tasks.find(args);
        ui.printFindResults(matches);
    }
}