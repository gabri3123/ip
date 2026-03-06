import java.util.Scanner;

/**
 * Handles interactions with the user, including reading input
 * and displaying output messages.
 */
public class Ui {
    private static final String LONG_LINE = "_________________________________\n";
    private final Scanner scanner;

    /**
     * Creates an Ui object that reads input from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reads a command entered by the user.
     *
     * @return user input line
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the welcome message and logo.
     */
    public void printWelcome() {
        String logo = " ____   ___   _   _   _   _   _  _ \n"
                + "|  _ \\ / _ \\ | \\ | | | \\ | | | \\/ |\n"
                + "| | | | | | ||  \\| | |  \\| | \\   /\n"
                + "| |_| | |_| || |\\  | | |\\  |  | | \n"
                + "|____/ \\___/ |_| \\_| |_| \\_|  |_|  \n";

        System.out.println("Hello! I'm \n" + logo + "\n" + "What can I do for you? \n");
    }

    /**
     * Displays the exit message shown when the user ends the program.
     */
    public void printExitMessage() {
        System.out.println(LONG_LINE + "See you again, bye!\n" + LONG_LINE);
    }

    /**
     * Displays an error message in a standard format.
     *
     * @param message message to display
     */
    public void printError(String message) {
        System.out.println(LONG_LINE);
        System.out.println(" " + message);
        System.out.println(LONG_LINE);
    }

    /**
     * Displays the result of marking or unmarking a task.
     *
     * @param header status message header
     * @param task affected task
     */
    public void printTaskStatusChange(String header, Task task) {
        System.out.println(LONG_LINE);
        System.out.println(" " + header + "\n");
        System.out.println("  " + task);
        System.out.println(LONG_LINE);
    }

    /**
     * Displays all tasks in the current task list.
     *
     * @param tasks task list to display
     */
    public void printList(TaskList tasks) {
        System.out.println(LONG_LINE);
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
        System.out.println(LONG_LINE);
    }

    /**
     * Displays confirmation after adding a task.
     *
     * @param task task that was added
     * @param size current number of tasks
     */
    public void printAdded(Task task, int size) {
        System.out.println(LONG_LINE);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + size + " tasks in the list.");
        System.out.println(LONG_LINE);
    }

    /**
     * Displays confirmation after deleting a task.
     *
     * @param removed task that was removed
     * @param size current number of tasks left
     */
    public void printDeleted(Task removed, int size) {
        System.out.println(LONG_LINE);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + removed);
        System.out.println(" Now you have " + size + " tasks in the list.");
        System.out.println(LONG_LINE);
    }

    /**
     * Displays the tasks matching a search keyword.
     *
     * @param tasks matching tasks
     */
    public void printFindResults(TaskList tasks) {
        System.out.println(LONG_LINE);
        System.out.println(" Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
        System.out.println(LONG_LINE);
    }
}