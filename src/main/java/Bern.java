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

    /** keywords for chatbot commands */
    private static final String KEYWORD_EXIT = "bye";
    private static final String KEYWORD_LIST_TASKS = "list";
    private static final String KEYWORD_TASK_MARK = "mark";
    private static final String KEYWORD_TASK_UNMARK = "unmark";

    /** task-related variables */
    private static Task[] tasks = new Task[100];
    private static int taskCount = 0;

    /**
     * Returns greeting message as a String.
     *
     * @return Greeting message to the user.
     */
    private static void greetUser() {
        String GREETING_TEMPLATE = "%s\n"
                + MESSAGE_LINE
                + "> Hello! I'm %s.\n"
                + "> What can I do for you?\n";
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
     * @param taskName The message to add
     */
    private static void addTask(String taskName) {
        if (taskCount == tasks.length) {
            System.out.print("> No more tasks can be added.\n" + MESSAGE_LINE);
            return;
        }

        tasks[taskCount++] = new Task(taskName);

        System.out.print("> added: " + taskName + "\n" + MESSAGE_LINE);
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
        System.out.print(sb.toString());
    }

    public static void main(String[] args) {
        greetUser();
        Scanner sc = new Scanner(System.in);
        boolean isReadingInput = true;
        while (isReadingInput) {
            String input = promptForInput(sc);
            String[] inputTokens = input.split(" ");

            switch (inputTokens[0].toLowerCase()) {
                case KEYWORD_EXIT:
                    if (inputTokens.length != 1) {
                        System.out.print(String.format("Incorrect usage of %s. Expected: %s\n" + MESSAGE_LINE, KEYWORD_EXIT, KEYWORD_EXIT));
                        break;
                    }
                    isReadingInput = false;
                    break;
                case KEYWORD_LIST_TASKS:
                    if (inputTokens.length != 1) {
                        System.out.print(String.format("Incorrect usage of %s. Expected: %s\n" + MESSAGE_LINE, KEYWORD_LIST_TASKS, KEYWORD_LIST_TASKS));
                        break;
                    }
                    listTasks();
                    break;
                case KEYWORD_TASK_MARK:
                    if (inputTokens.length != 2) {
                        System.out.print(String.format("Incorrect usage of %s. Expected: %s [integer]\n" + MESSAGE_LINE, KEYWORD_TASK_MARK, KEYWORD_TASK_MARK));
                        break;
                    }
                    try {
                        int taskIndex = Integer.parseInt(inputTokens[1]);

                        if (taskCount == 0) {
                            System.out.print(String.format("There are no tasks to mark.\n" + MESSAGE_LINE));
                            break;
                        }

                        if (taskIndex < 1 || taskIndex > taskCount) {
                            System.out.print(String.format("Given task number must be from %d to %d\n" + MESSAGE_LINE, 1, taskCount));
                            break;
                        }

                        Task targetTask = tasks[taskIndex - 1];
                        if (targetTask.isDone()) {
                            System.out.print(String.format("The following task is already marked as done:\n%s\n%s",
                                    targetTask,
                                    MESSAGE_LINE
                            ));
                            break;
                        }

                        tasks[taskIndex - 1].setDone(true);

                        System.out.print(String.format("Nice! I've marked this task as done:\n%s\n%s",
                                tasks[taskIndex - 1],
                                MESSAGE_LINE
                        ));

                    } catch (NumberFormatException e) {
                        System.out.print(String.format("Argument after %s must correspond to an existing task number\n" + MESSAGE_LINE, KEYWORD_TASK_MARK));
                        break;
                    }
                    break;
                case KEYWORD_TASK_UNMARK:
                    if (inputTokens.length != 2) {
                        System.out.print(String.format("Incorrect usage of %s. Expected: %s [integer]\n" + MESSAGE_LINE, KEYWORD_TASK_MARK, KEYWORD_TASK_MARK));
                        break;
                    }
                    try {
                        int taskIndex = Integer.parseInt(inputTokens[1]);

                        if (taskCount == 0) {
                            System.out.print(String.format("There are no tasks to unmark.\n" + MESSAGE_LINE));
                            break;
                        }

                        if (taskIndex < 1 || taskIndex > taskCount) {
                            System.out.print(String.format("Given task number must be from %d to %d\n" + MESSAGE_LINE, 1, taskCount));
                            break;
                        }

                        Task targetTask = tasks[taskIndex - 1];
                        if (!targetTask.isDone()) {
                            System.out.print(String.format("The following task is already marked as undone:\n%s\n%s",
                                    targetTask,
                                    MESSAGE_LINE
                            ));
                            break;
                        }

                        tasks[taskIndex - 1].setDone(false);

                        System.out.print(String.format("OK, I've marked this task as not done yet:\n%s\n%s",
                                tasks[taskIndex - 1],
                                MESSAGE_LINE
                        ));

                    } catch (NumberFormatException e) {
                        System.out.print(String.format("Argument after %s must correspond to an existing task number\n" + MESSAGE_LINE, KEYWORD_TASK_MARK));
                        break;
                    }
                    break;
                default:
                    addTask(input);
            }
        }

        sc.close();
        exitBot();
    }
}
