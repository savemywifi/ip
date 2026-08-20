import java.text.ParseException;
import java.util.Scanner;

public class Bern {
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
    private enum Keyword {
        BYE,
        LIST,
        MARK, UNMARK,
        TODO, DEADLINE, EVENT
    }

    /** task-related variables */
    private static final Task[] tasks = new Task[100];
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

    private static String getParseExceptionResponse(ParseException e) {
        if (e.getErrorOffset() == -1) {
            // Missing argument
            return String.format("Argument(s) for %s keyword not specified\n", e.getMessage());
        }
        return String.format("Missing keyword: %s\n", e.getMessage());
    }

    private static boolean attemptExitAction(String[] inputTokens) {
        if (inputTokens.length != 1) {
            printMessage(String.format("Incorrect usage of %s. Expected: %s", Keyword.BYE, Keyword.BYE));
            return false;
        }
        return true;
    }

    private static boolean attemptListTasksAction(String[] inputTokens) {
        if (inputTokens.length != 1) {
            printMessage(String.format("Incorrect usage of %s. Expected: %s", Keyword.LIST, Keyword.LIST));
            return false;
        }
        listTasks();
        return true;
    }

    private static int tryGetTaskNumber(String[] inputTokens) {
        if (taskCount == 0) {
            printMessage("There are no tasks.");
            return -1;
        }

        if (inputTokens.length != 2) {
            printMessage(String.format("Incorrect usage of %s. Expected %s [task number]", inputTokens[0], inputTokens[0]));
            return -1;
        }

        int taskIndex;

        try {
            taskIndex = Integer.parseInt(inputTokens[1]);
        } catch (NumberFormatException e) {
            printMessage(String.format("Argument after %s must correspond to an existing task number from 1 to %d", inputTokens[0], taskCount));
            return -1;
        }

        if (taskIndex < 1 || taskIndex > taskCount) {
            printMessage(String.format("Given task number must be from %d to %d", 1, taskCount));
            return -1;
        }

        return taskIndex;
    }

    private static boolean attemptMarkTask(String[] inputTokens) {
        int taskNumber = tryGetTaskNumber(inputTokens);
        if (taskNumber == -1) {
            return false;
        }

        Task target = tasks[taskNumber - 1];
        if (target.isDone()) {
            printMessage("The following task is already marked as done:\n" + target);
            return false;
        }

        target.setDone(true);
        printMessage("Nice! I've marked this task as done:\n" + target);
        return true;
    }

    private static boolean attemptUnmarkTask(String[] inputTokens) {
        int taskNumber = tryGetTaskNumber(inputTokens);
        if (taskNumber == -1) {
            return false;
        }

        Task target = tasks[taskNumber - 1];
        if (!target.isDone()) {
            printMessage("The following task is already marked as undone:\n" + target);
            return false;
        }

        target.setDone(false);
        printMessage("OK, I've marked this task as not done yet:\n" + target);
        return true;
    }

    public static void main(String[] args) {
        greetUser();

        Scanner sc = new Scanner(System.in);
        boolean isReadingInput = true;
        Keyword keyword;

        while (isReadingInput) {
            String input = promptForInput(sc);
            String[] inputTokens = input.split(" ");

            try {
                keyword = Keyword.valueOf(inputTokens[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                printMessage("Command not recognised. List of commands: todo, deadline, event, mark, unmark, list");
                continue;
            }

            switch (keyword) {
                case Keyword.BYE:
                    isReadingInput = !attemptExitAction(inputTokens);
                    break;
                case Keyword.LIST:
                    attemptListTasksAction(inputTokens);
                    break;
                case Keyword.MARK:
                    attemptMarkTask(inputTokens);
                    break;
                case Keyword.UNMARK:
                    attemptUnmarkTask(inputTokens);
                    break;
                case Keyword.TODO:
                    try {
                        addTask(TaskFactory.makeTodo(inputTokens));
                    } catch (ParseException e) {
                        printMessage(getParseExceptionResponse(e) + "Command syntax: todo <task name>");
                    }
                    break;
                case Keyword.DEADLINE:
                    try {
                        addTask(TaskFactory.makeDeadline(inputTokens));
                    } catch (ParseException e) {
                        printMessage(getParseExceptionResponse(e)
                                + "Command syntax: deadline <task name> /by <due date>");
                    }
                    break;
                case Keyword.EVENT:
                    try {
                        addTask(TaskFactory.makeEvent(inputTokens));
                    } catch (ParseException e) {
                        printMessage(getParseExceptionResponse(e)
                                + "Command syntax: event <task name> /from <start date time> /to <end date time>");
                    }
                    break;
                default:
                    printMessage("Command not recognised.\n"
                            + "List of commands: todo, deadline, event, mark, unmark, list, bye");
                    break;
            }
        }

        sc.close();
        exitBot();
    }
}
