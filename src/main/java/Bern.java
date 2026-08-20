import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;

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

    /** Chatbot name */
    private static final String CHATBOT_NAME = "Bern Tokens";

    /** Enum containing keywords */
    private enum Keyword {
        BYE(Bern::attemptExit),
        LIST(Bern::attemptListTasks),
        MARK(Bern::attemptMarkTask), UNMARK(Bern::attemptUnmarkTask),
        TODO(Bern::attemptMakeTodo), DEADLINE(Bern::attemptMakeDeadline), EVENT(Bern::attemptMakeEvent), DELETE(Bern::attemptDeleteTask);

        private final Consumer<String[]> action;

        Keyword(Consumer<String[]> action) {
            this.action = action;
        }
    }

    /** task-related variables */
    private static final ArrayList<Task> tasks = new ArrayList<Task>();

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
        tasks.add(task);

        printMessage("> added: " + task);
    }

    /**
     * Lists the stored tasks in the standard output
     */
    private static void listTasks() {
        if (tasks.isEmpty()) {
            printMessage("> You have no tasks.");
            return;
        }

        System.out.print("> Here are your current tasks: ");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("\n%d. %s", i + 1, tasks.get(i)));
        }
        printMessage(sb.toString());
    }

    /**
     * Takes the parse exception passed from TaskFactory and parses it into an appropriate error message
     *
     * @param e The received parse exception to parse
     * @return An error message reflecting the exception details
     */
    private static String getParseExceptionResponse(ParseException e) {
        if (e.getErrorOffset() == -1) {
            // Missing argument
            return String.format("Argument(s) for %s keyword not specified\n", e.getMessage());
        }
        return String.format("Missing keyword: %s\n", e.getMessage());
    }

    /**
     * Attempt to exit the program, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return A boolean indicating the success of the operation
     */
    private static boolean attemptExit(String[] inputTokens) {
        if (inputTokens.length != 1) {
            printMessage(String.format("Incorrect usage of %s. Expected: %s", Keyword.BYE, Keyword.BYE));
            return false;
        }
        return true;
    }

    /**
     * Attempt to list tasks, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return A boolean indicating the success of the operation
     */
    private static boolean attemptListTasks(String[] inputTokens) {
        if (inputTokens.length != 1) {
            printMessage(String.format("Incorrect usage of %s. Expected: %s", Keyword.LIST, Keyword.LIST));
            return false;
        }
        listTasks();
        return true;
    }

    /**
     * Attempt to extract a task number from the second argument. Assumes only two tokens are given and rejects all
     * other inputs
     *
     * @param inputTokens The tokens in the input
     *
     * @return An integer with the task number, or -1 if the number is invalid
     */
    private static int tryGetTaskNumber(String[] inputTokens) {
        if (tasks.isEmpty()) {
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
            printMessage(String.format("Argument after %s must correspond to an existing task number from 1 to %d", inputTokens[0], tasks.size()));
            return -1;
        }

        if (taskIndex < 1 || taskIndex > tasks.size()) {
            printMessage(String.format("Given task number must be from %d to %d", 1, tasks.size()));
            return -1;
        }

        return taskIndex;
    }

    /**
     * Attempt to mark a task as done, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return A boolean indicating the success of this operation
     */
    private static boolean attemptMarkTask(String[] inputTokens) {
        int taskNumber = tryGetTaskNumber(inputTokens);
        if (taskNumber == -1) {
            return false;
        }

        Task target = tasks.get(taskNumber - 1);
        if (target.isDone()) {
            printMessage("The following task is already marked as done:\n" + target);
            return false;
        }

        target.setDone(true);
        printMessage("Nice! I've marked this task as done:\n" + target);
        return true;
    }

    /**
     * Attempt to mark a task as undone, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return A boolean indicating the success of this operation
     */
    private static boolean attemptUnmarkTask(String[] inputTokens) {
        int taskNumber = tryGetTaskNumber(inputTokens);
        if (taskNumber == -1) {
            return false;
        }

        Task target = tasks.get(taskNumber - 1);
        if (!target.isDone()) {
            printMessage("The following task is already marked as undone:\n" + target);
            return false;
        }

        target.setDone(false);
        printMessage("OK, I've marked this task as not done yet:\n" + target);
        return true;
    }

    /**
     * Attempt to make and add a todo, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return A boolean indicating the success of this operation
     */
    private static boolean attemptMakeTodo(String[] inputTokens) {
        try {
            addTask(TaskFactory.makeTodo(inputTokens));
        } catch (ParseException e) {
            printMessage(getParseExceptionResponse(e) + "Command syntax: todo <task name>");
            return false;
        }
        return true;
    }

    /**
     * Attempt to make and add a deadline, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return A boolean indicating the success of this operation
     */
    private static boolean attemptMakeDeadline(String[] inputTokens) {
        try {
            addTask(TaskFactory.makeDeadline(inputTokens));
        } catch (ParseException e) {
            printMessage(getParseExceptionResponse(e)
                    + "Command syntax: deadline <task name> /by <due date>");
            return false;
        }
        return true;
    }

    /**
     * Attempt to make and add an event, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return A boolean indicating the success of this operation
     */
    private static boolean attemptMakeEvent(String[] inputTokens) {
        try {
            addTask(TaskFactory.makeEvent(inputTokens));
        } catch (ParseException e) {
            printMessage(getParseExceptionResponse(e)
                    + "Command syntax: event <task name> /from <start date time> /to <end date time>");
            return false;
        }
        return true;
    }

    /**
     * Attempt to delete a task, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return A boolean indicating the success of this operation
     */
    private static boolean attemptDeleteTask(String[] inputTokens) {
        int taskNumber = tryGetTaskNumber(inputTokens);
        if (taskNumber == -1) {
            return false;
        }

        Task target = tasks.get(taskNumber - 1);
        tasks.remove(taskNumber - 1);
        printMessage("OK, I've removed this task:\n" + target);
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
                printMessage("Command not recognised.\n"
                        + "List of commands: todo, deadline, event, mark, unmark, delete, list, bye");
                continue;
            }

            keyword.action.accept(inputTokens);

            if (keyword.equals(Keyword.BYE) && inputTokens.length == 1) {
                isReadingInput = false;
            }
        }

        sc.close();
        exitBot();
    }
}
