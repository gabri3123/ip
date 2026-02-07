import java.util.Scanner;

public class Donny {
    public static void main(String[] args) {
        String logo = " ____   ___   _   _   _   _   _  _ \n"
                + "|  _ \\ / _ \\ | \\ | | | \\ | | | \\/ |\n"
                + "| | | | | | ||  \\| | |  \\| | \\   /\n"
                + "| |_| | |_| || |\\  | | |\\  |  | | \n"
                + "|____/ \\___/ |_| \\_| |_| \\_|  |_|  \n";
        String longline = "_________________________________\n";
        System.out.println("Hello! I'm \n" + logo + "\n" + "What can I do for you? \n");

        String line;
        Scanner in = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int size = 0;

        boolean[] done = new boolean[100];

        while (true) {
            line = in.nextLine();

            if (line.equalsIgnoreCase("list")) {

                System.out.println(longline);
                System.out.println(" Here are the tasks in your list:");

                for (int i = 0; i < size; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }

                System.out.println(longline);
                continue;
            }

            if (line.toLowerCase().startsWith("mark")){

                String[]parts = line.split("\\s+");
                int index = Integer.parseInt(parts[1]) - 1;

                tasks[index].markAsDone();

                System.out.println(longline);
                System.out.println(" Nice! I've marked this task as done:\n");
                System.out.println("  [X]  " + tasks[index].getDescription());
                continue;
            }

            if (line.toLowerCase().startsWith("unmark")) {

                String[] parts = line.split("\\s+");
                int index = Integer.parseInt(parts[1]) - 1;

                tasks[index].markAsNotDone();

                System.out.println(longline);
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   [ ] " + tasks[index].getDescription());
                continue;
            }

            if (line.toLowerCase().startsWith("todo")) {

                String desc = line.substring(5);
                tasks[size++] = new Todo(desc);

                System.out.println(longline);
                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + tasks[size-1]);
                System.out.println(" Now you have " + size + " tasks in the list.");
                System.out.println(longline);
                continue;
            }

            if (line.toLowerCase().startsWith("deadline")) {

                String[] parts = line.substring(9).split("/by");

                tasks[size++] = new Deadline(parts[0].trim(), parts[1].trim());

                System.out.println(longline);
                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + tasks[size-1]);
                System.out.println(" Now you have " + size + " tasks in the list.");
                System.out.println(longline);
                continue;
            }

            if (line.toLowerCase().startsWith("event")) {

                String[] parts = line.substring(6).split("/from|/to");

                tasks[size++] = new Event(parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim());

                System.out.println(longline);
                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + tasks[size-1]);
                System.out.println(" Now you have " + size + " tasks in the list.");
                System.out.println(longline);
                continue;
            }

            if (line.equalsIgnoreCase("bye")) {
                System.out.println(longline + "See you again, bye!\n" + longline);
                break;
            }

            tasks[size] = new Task(line);
            size++;
            System.out.println(longline + "added:" + line + "\n");
        }
    }
}
