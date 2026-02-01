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

        while (true) {
            line = in.nextLine();
            System.out.println(longline + line + "\n");
            if (line.equalsIgnoreCase("bye")) {
                System.out.println(longline + "See you again, bye!\n" + longline);
                break;
            }
        }
    }
}
