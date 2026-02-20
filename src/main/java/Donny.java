import java.util.ArrayList;
import java.util.Scanner;

public class Donny {
    private static final String LONG_LINE = "_________________________________\n";

    public static void main(String[] args) {
        String logo = " ____   ___   _   _   _   _   _  _ \n"
                + "|  _ \\ / _ \\ | \\ | | | \\ | | | \\/ |\n"
                + "| | | | | | ||  \\| | |  \\| | \\   /\n"
                + "| |_| | |_| || |\\  | | |\\  |  | | \n"
                + "|____/ \\___/ |_| \\_| |_| \\_|  |_|  \n";

        System.out.println("Hello! I'm \n" + logo + "\n" + "What can I do for you? \n");

        Scanner in = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        while (true) {
            String line = in.nextLine();

            try {
                if (line.equalsIgnoreCase("bye")) {
                    System.out.println(LONG_LINE + "See you again, bye!\n" + LONG_LINE);
                    break;
                }

                if (line.equalsIgnoreCase("list")) {
                    printList(tasks);
                    continue;
                }

                if (line.toLowerCase().startsWith("mark")) {
                    int index = parseIndex(line, tasks.size());
                    tasks.get(index).markAsDone();
                    System.out.println(LONG_LINE);
                    System.out.println(" Nice! I've marked this task as done:\n");
                    System.out.println("  " + tasks.get(index));
                    System.out.println(LONG_LINE);
                    continue;
                }

                if (line.toLowerCase().startsWith("unmark")) {
                    int index = parseIndex(line, tasks.size());
                    tasks.get(index).markAsNotDone();
                    System.out.println(LONG_LINE);
                    System.out.println(" OK, I've marked this task as not done yet:\n");
                    System.out.println("  " + tasks.get(index));
                    System.out.println(LONG_LINE);
                    continue;
                }

                if (line.toLowerCase().startsWith("delete")) {
                    int index = parseIndex(line, tasks.size());
                    Task removed = tasks.remove(index);

                    System.out.println(LONG_LINE);
                    System.out.println(" Noted. I've removed this task:");
                    System.out.println("   " + removed);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(LONG_LINE);
                    continue;
                }

                if (line.toLowerCase().startsWith("todo")) {
                    String desc = line.length() > 4 ? line.substring(4).trim() : "";
                    if (desc.isEmpty()) {
                        throw new DonnyException("I need a description for a todo. Try: todo read book");
                    }

                    tasks.add(new Todo(desc));
                    printAdded(tasks.get(tasks.size() - 1), tasks.size());
                    continue;
                }

                if (line.toLowerCase().startsWith("deadline")) {
                    String rest = line.length() > 8 ? line.substring(8).trim() : "";
                    if (rest.isEmpty()) {
                        throw new DonnyException("I need a description and /by for a deadline. Try: deadline return book /by Sunday");
                    }

                    String[] parts = rest.split("\\s*/by\\s*", 2);
                    if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                        throw new DonnyException("Deadline format should be: deadline <description> /by <time>");
                    }

                    tasks.add(new Deadline(parts[0].trim(), parts[1].trim()));
                    printAdded(tasks.get(tasks.size() - 1), tasks.size());
                    continue;
                }

                if (line.toLowerCase().startsWith("event")) {
                    String rest = line.length() > 5 ? line.substring(5).trim() : "";
                    if (rest.isEmpty()) {
                        throw new DonnyException("I need an event with /from and /to. Try: event meeting /from Mon 2pm /to Mon 4pm");
                    }

                    // Split once on /from, then once on /to (safer than regex split with multiple delimiters)
                    String[] first = rest.split("\\s*/from\\s*", 2);
                    if (first.length < 2 || first[0].trim().isEmpty()) {
                        throw new DonnyException("Event format should be: event <description> /from <start> /to <end>");
                    }
                    String description = first[0].trim();

                    String[] second = first[1].split("\\s*/to\\s*", 2);
                    if (second.length < 2 || second[0].trim().isEmpty() || second[1].trim().isEmpty()) {
                        throw new DonnyException("Event format should be: event <description> /from <start> /to <end>");
                    }

                    tasks.add(new Event(description, second[0].trim(), second[1].trim()));
                    printAdded(tasks.get(tasks.size() - 1), tasks.size());
                    continue;
                }

                // If it reaches here, it’s an unknown command
                throw new DonnyException("I don't understand that command. Try: list, todo, deadline, event, mark, unmark, bye");

            } catch (DonnyException e) {
                System.out.println(LONG_LINE);
                System.out.println(" OOPS! " + e.getMessage());
                System.out.println(LONG_LINE);
            } catch (Exception e) {
                // Safety net for unexpected crashes (stretch goal-ish)
                System.out.println(LONG_LINE);
                System.out.println(" OOPS! Something went wrong. Please check your input format.");
                System.out.println(LONG_LINE);
            }
        }
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

    private static int parseIndex(String line, int size) throws DonnyException {
        String[] parts = line.trim().split("\\s+");
        if (parts.length < 2) {
            throw new DonnyException("Please specify a task number. Example: mark 2");
        }

        int num;
        try {
            num = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new DonnyException("That task number isn't a valid integer. Example: mark 2");
        }

        int index = num - 1;
        if (index < 0 || index >= size) {
            throw new DonnyException("Task number out of range. Use: list (then pick 1 to " + size + ")");
        }
        return index;
    }
}
