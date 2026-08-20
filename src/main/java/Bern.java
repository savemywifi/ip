import java.text.ParseException;
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
    private static final String KEYWORD_ADD_TASK_TODO = "todo";
    private static final String KEYWORD_ADD_TASK_DEADLINE = "deadline";
    private static final String KEYWORD_ADD_TASK_EVENT = "event";

    /** task-related variables */
    private static Task[] tasks = new Task[100];
    private static int taskCount = 0;

    /**
     * Prints a message to the standard output, appended with a message line
     *
     * @param msg The message to be printed
     */
    private static void printMessage(String msg) {
        System.out.print(msg + "\n" + MESSAGE_LINE);
    }

    /**
     * Returns greeting message as a String.
     *
     * @return Greeting message to the user.
     */
    private static void greetUser() {
        String GREETING_TEMPLATE = "> Hello! I'm %s.\n"
                + "> What can I do for you?";
        System.out.print(MESSAGE_LINE);
        printMessage(CHATBOT_BANNER);
        printMessage(String.format(GREETING_TEMPLATE, CHATBOT_NAME));
    }

    /**
     * Returns exit message as a String.
     *
     * @return Exit message to the user.
     */
    private static void exitBot() {
        printMessage("> Bye. Hope to see you again soon!");
        System.out.print(MESSAGE_LINE);
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

        while (input.isEmpty()) {
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
        printMessage("> " + msg);
    }

    /**
     * Adds a given task to the list of tasks
     *
     * @param task The task to add
     */
    private static void addTask(Task task) {
        if (taskCount == tasks.length) {
            printMessage("> No more tasks can be added.\n");
            return;
        }

        tasks[taskCount++] = task;

        printMessage("> added: " + task);
    }

    /**
     * Lists the stored tasks in the standard output
     */
    private static void listTasks() {
        if (taskCount <= 0) {
            printMessage("> You have no tasks.");
            return;
        }

        System.out.print("> Here are your current tasks: ");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < taskCount; i++) {
            sb.append(String.format("\n%d. %s", i + 1, tasks[i]));
        }
        printMessage(sb.toString());
    }

    private static String getParseExceptionResponse(ParseException e, String[] inputTokens) {
        if (e.getErrorOffset() == -1) {
            // Missing argument
            return String.format("Argument(s) for %s keyword not specified\n", e.getMessage());
        }
        return String.format("Missing keyword: %s\n", e.getMessage());
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
                        printMessage(String.format("Incorrect usage of %s. Expected: %s", KEYWORD_EXIT, KEYWORD_EXIT));
                        break;
                    }
                    isReadingInput = false;
                    break;
                case KEYWORD_LIST_TASKS:
                    if (inputTokens.length != 1) {
                        printMessage(String.format("Incorrect usage of %s. Expected: %s", KEYWORD_LIST_TASKS, KEYWORD_LIST_TASKS));
                        break;
                    }
                    listTasks();
                    break;
                case KEYWORD_TASK_MARK:
                    if (inputTokens.length != 2) {
                        printMessage(String.format("Incorrect usage of %s. Expected: %s [task number]", KEYWORD_TASK_MARK, KEYWORD_TASK_MARK));
                        break;
                    }
                    try {
                        int taskIndex = Integer.parseInt(inputTokens[1]);

                        if (taskCount == 0) {
                            printMessage(String.format("There are no tasks to mark."));
                            break;
                        }

                        if (taskIndex < 1 || taskIndex > taskCount) {
                            printMessage(String.format("Given task number must be from %d to %d", 1, taskCount));
                            break;
                        }

                        Task targetTask = tasks[taskIndex - 1];
                        if (targetTask.isDone()) {
                            printMessage("The following task is already marked as done:\n" + targetTask);
                            break;
                        }

                        targetTask.setDone(true);
                        printMessage("Nice! I've marked this task as done:\n" + targetTask);

                    } catch (NumberFormatException e) {
                        printMessage(String.format("Argument after %s must correspond to an existing task number", KEYWORD_TASK_MARK));
                        break;
                    }
                    break;
                case KEYWORD_TASK_UNMARK:
                    if (inputTokens.length != 2) {
                        printMessage(String.format("Incorrect usage of %s. Expected: %s [task number]", KEYWORD_TASK_MARK, KEYWORD_TASK_MARK));
                        break;
                    }
                    try {
                        int taskIndex = Integer.parseInt(inputTokens[1]);

                        if (taskCount == 0) {
                            printMessage(String.format("There are no tasks to unmark."));
                            break;
                        }

                        if (taskIndex < 1 || taskIndex > taskCount) {
                            printMessage(String.format("Given task number must be from %d to %d", 1, taskCount));
                            break;
                        }

                        Task targetTask = tasks[taskIndex - 1];
                        if (!targetTask.isDone()) {
                            printMessage("The following task is already marked as undone:\n" + targetTask);
                            break;
                        }

                        targetTask.setDone(false);

                        printMessage("OK, I've marked this task as not done yet:\n" + targetTask);
                    } catch (NumberFormatException e) {
                        System.out.print(String.format("Argument after %s must correspond to an existing task number\n" + MESSAGE_LINE, KEYWORD_TASK_MARK));
                        break;
                    }
                    break;
                case KEYWORD_ADD_TASK_TODO:
                    try {
                        addTask(TaskFactory.makeTodo(inputTokens));
                    } catch (ParseException e) {
                        printMessage(getParseExceptionResponse(e, inputTokens)
                                + "Command syntax: todo <task name>");
                    }
                    break;
                case KEYWORD_ADD_TASK_DEADLINE:
                    try {
                        addTask(TaskFactory.makeDeadline(inputTokens));
                    } catch (ParseException e) {

                        printMessage(getParseExceptionResponse(e, inputTokens)
                                + "Command syntax: deadline <task name> /by <due date>");
                    }
                    break;
                case KEYWORD_ADD_TASK_EVENT:
                    try {
                        addTask(TaskFactory.makeEvent(inputTokens));
                    } catch (ParseException e) {
                        printMessage(getParseExceptionResponse(e, inputTokens)
                                + "Command syntax: event <task name> /from <start date time> /to <end date time>");
                    }
                    break;
                default:
                    printMessage("Command not recognised. List of commands: todo, deadline, event, mark, unmark, list");
                    break;
            }
        }

        sc.close();
        exitBot();
    }
}
