import java.util.Scanner;

public class Bern {
    private static final String MESSAGE_LINE = "____________________________________\n";
    private static final String CHATBOT_BANNER = " ____                  \n"
            + "|  _ \\                 \n"
            + "| |_) | ___ _ __ _ __  \n"
            + "|  _ < / _ \\ '__| '_ \\ \n"
            + "| |_) |  __/ |  | | | |\n"
            + "|____/ \\___|_|  |_| |_|";
    private static final String CHATBOT_NAME = "Bern Tokens";
    private static final String GREETING_TEMPLATE = "%s\n"
            + MESSAGE_LINE
            + "> Hello! I'm %s.\n"
            + "> What can I do for you?\n";
    private static final String KEYWORD_EXIT = "bye";
    /**
     * Returns greeting message as a String.
     *
     * @return Greeting message to the user.
     */
    private static String getGreetingMessage() {
        return MESSAGE_LINE + String.format(GREETING_TEMPLATE, CHATBOT_BANNER, CHATBOT_NAME) + MESSAGE_LINE;
    }

    /**
     * Returns exit message as a String.
     *
     * @return Exit message to the user.
     */
    private static String getExitMessage() {
        return "> Bye. Hope to see you again soon!\n" + MESSAGE_LINE + MESSAGE_LINE;
    }

    /**
     * Prompts the user for an input, then closes the input with a message line
     *
     * @param sc Scanner to receive input from
     *
     * @return Received input, stripped of whitespace
     */
    private static String promptForInput(Scanner sc) {
        String input = "";

        while (input.length() <= 0) {
            input = sc.nextLine().strip();
        }

        System.out.print(MESSAGE_LINE);
        return input;
    }

    /**
     * Echoes the given message to the standard output
     *
     * @param msg The message to echo
     */
    private static void echoMessage(String msg) {
        System.out.print("> " + msg + "\n" + MESSAGE_LINE);
    }


    public static void main(String[] args) {
        System.out.print(getGreetingMessage());

        Scanner sc = new Scanner(System.in);

        while (true) {
            String input = promptForInput(sc);

            if (input.equalsIgnoreCase(KEYWORD_EXIT)) {
                break;
            }

            echoMessage(input);
        }

        sc.close();
        System.out.print(getExitMessage());
    }
}
