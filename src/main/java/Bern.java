import java.util.Scanner;

public class Bern {
    // message string-related constants
    /** Line that bookends every message */
    private static final String MESSAGE_LINE = "____________________________________\n";

    /** Constants for chatbot identity */
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

    /** keywords for chatbot commands */
    private static final String KEYWORD_EXIT = "bye";
    private static final String KEYWORD_LIST_TASKS = "list";

    /** task-related variables */
    private static String[] tasks = new String[100];
    private static int taskCount = 0;

    /**
     * Returns greeting message as a String.
     *
     * @return Greeting message to the user.
     */
    private static void greetUser() {
        System.out.print(MESSAGE_LINE + String.format(GREETING_TEMPLATE, CHATBOT_BANNER, CHATBOT_NAME) + MESSAGE_LINE);
    }

    /**
     * Returns exit message as a String.
     *
     * @return Exit message to the user.
     */
    private static void exitBot() {
        System.out.print("> Bye. Hope to see you again soon!\n" + MESSAGE_LINE + MESSAGE_LINE);
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

    /**
     * Adds the given message to a list of text, then prints the action
     *
     * @param task The message to add
     */
    private static void addTask(String task) {
        if (taskCount == tasks.length) {
            System.out.print("> No more tasks can be added.\n" + MESSAGE_LINE);
            return;
        }

        tasks[taskCount++] = task;

        System.out.print("> added: " + task + "\n" + MESSAGE_LINE);
    }

    /**
     * Lists the stored tasks in the standard output
     */
    private static void listTasks() {
        if (taskCount <= 0) {
            System.out.print("> You have no tasks.\n" + MESSAGE_LINE);
            return;
        }

        System.out.println("> Here are your current tasks: ");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < taskCount; i++) {
            sb.append(String.format("%d. %s\n", i + 1, tasks[i]));
        }
        sb.append(MESSAGE_LINE);
        System.out.println(sb.toString());
    }


    public static void main(String[] args) {
        greetUser();
        Scanner sc = new Scanner(System.in);

        while (true) {
            String input = promptForInput(sc);

            if (input.equalsIgnoreCase(KEYWORD_EXIT)) {
                break;
            } else if (input.equalsIgnoreCase(KEYWORD_LIST_TASKS)) {
                listTasks();
            } else {
                addTask(input);
            }
        }

        sc.close();
        exitBot();
    }
}
