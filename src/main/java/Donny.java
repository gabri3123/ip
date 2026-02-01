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
        String[] list = new String[100];
        int size = 0;

        while (true) {
            line = in.nextLine();

            if (line.equalsIgnoreCase("list")) {
                System.out.println(longline);
                for (int i = 0; i < size; i++) {
                    System.out.println((i + 1) + "." + list[i] + "\n");
                }
                System.out.println(longline);
                continue;
            }

            if (line.equalsIgnoreCase("bye")) {
                System.out.println(longline + "See you again, bye!\n" + longline);
                break;
            }

            list[size] = line;
            size ++;
            System.out.println(longline + "added:" + line + "\n");
        }
    }
}
