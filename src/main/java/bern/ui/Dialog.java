package bern.ui;

import java.util.Scanner;

/** Handles user-facing input prompts and output messages. */
public class Dialog {
    private static Dialog instance;

    /** Message line printed after user-facing output. */
    private static final String MESSAGE_LINE = "____________________________________\n";
    private static final String MESSAGE_TASK_LOADED = "Loaded saved tasks. Use list to view them.";
    private static final String MESSAGE_GOODBYE = "Bye. Hope to see you again soon!";

    /** ASCII-art banner intended for chatbot identification. */
    private static final String CHATBOT_BANNER = """
             ____                 \s
            |  _ \\                \s
            | |_) | ___ _ __ _ __ \s
            |  _ < / _ \\ '__| '_ \\\s
            | |_) |  __/ |  | | | |
            |____/ \\___|_|  |_| |_|""";

    /** Name displayed for the chatbot. */
    private static final String CHATBOT_NAME = "Bern Tokens";

    /** Template used for the greeting. */
    private static final String TEMPLATE_GREETING = "Hello! I'm %s. \n"
            + "What can I do for you?";

    /** Error message for an unknown command. */
    private static final String ERROR_KEYWORD_INVALID = "Command not recognised.\n"
            + "List of commands: todo, deadline, event, mark, unmark, delete, list, bye";

    private static final String ERROR_DIRECTORY = "Unable to create directory for save file";
    private static final String ERROR_SAVE = "Unable to save task data.";
    private static final String ERROR_LOAD = "Couldn't access a previous save for some reason.";
    private static final String ERROR_LOAD_TASK = "Some tasks were unable to be retrieved.";

    private static final String ERROR_NO_TASKS = "There are no tasks.";
    private static final String ERROR_TEMPLATE_INVALID_TASK_NUMBER = "Given task number must be from 1 to %d";
    private static final String ERROR_TEMPLATE_INCORRECT_KEYWORD_USAGE = "Incorrect usage of %s. Expected: %s";

    private static final String ERROR_TEMPLATE_INVALID_DATE_TIME = "%s is not a valid date or time";

    private Dialog() {
    }

    /**
     * Creates the singleton instance of the Dialog class if it doesn't already exist, then returns it.
     *
     * @return An instance of the Dialog class.
     */
    public static Dialog getInstance() {
        if (instance == null) {
            instance = new Dialog();
        }

        return instance;
    }

    /**
     * Reads the next non-blank input line, strips leading and trailing whitespace, and prints a separator afterward.
     *
     * @param sc Scanner to receive input from.
     *
     * @return Received input, stripped of whitespace.
     */
    public String promptForInput(Scanner sc) {
        String input = "";

        while (input.isEmpty()) {
            input = sc.nextLine().strip();
        }

        System.out.print(MESSAGE_LINE);
        return input;
    }

    /**
     * The message the user is presented with on startup of the chatbot
     *
     * @return The greeting message displayed to the user.
     */
    public String greetUser() {
        return printMessage(String.format(TEMPLATE_GREETING, CHATBOT_NAME));
    }

    /**
     * The message displayed to the user if tasks are loaded on startup
     *
     * @return The loaded-task message displayed to the user.
     */
    public String printLoadedTasks() {
        return printMessage(MESSAGE_TASK_LOADED);
    }

    /**
     * The message displayed when the bot closes
     *
     * @return The farewell message displayed to the user.
     */
    public String sayGoodbye() {
        return printMessage(MESSAGE_GOODBYE);
    }

    //TODO: reframe to keyword
    /**
     * Displays a message showing correct use of a specified keyword
     *
     * @param keyword A keyword to be used by the user
     * @param expected The expected usage of this keyword
     * @return The usage error message displayed to the user.
     */
    public String printIncorrectKeywordUsageError(Object keyword, Object expected) {
        return printMessage(String.format(ERROR_TEMPLATE_INCORRECT_KEYWORD_USAGE, keyword, expected));
    }

    /**
     * Displays a message showing that there are no tasks
     *
     * @return The no-tasks error message displayed to the user.
     */
    public String printNoTasksError() {
        return printMessage(ERROR_NO_TASKS);
    }

    /**
     * Displays a message showing the range of valid task numbers
     *
     * @param taskCount The number of valid tasks.
     * @return The invalid-task-number error message displayed to the user.
     */
    public String printInvalidTaskNumberError(int taskCount) {
        return printMessage(String.format(ERROR_TEMPLATE_INVALID_TASK_NUMBER, taskCount));
    }

    /**
     * Displays a message showing the user's invalid date-time input
     *
     * @param invalidDateTime The invalid date-time input given by the user
     * @return The invalid-date-time error message displayed to the user.
     */
    public String printInvalidDateTimeError(String invalidDateTime) {
        return printMessage(String.format(ERROR_TEMPLATE_INVALID_DATE_TIME, invalidDateTime));
    }

    /**
     * Displays an error for a directory that cannot be created.
     *
     * @return The directory-error message displayed to the user.
     */
    public String printDirectoryError() {
        return printMessage(ERROR_DIRECTORY);
    }

    /**
     * Displays an error for task data that cannot be saved.
     *
     * @return The save-error message displayed to the user.
     */
    public String printSaveError() {
        return printMessage(ERROR_SAVE);
    }

    /**
     * Displays an error for task data that cannot be loaded.
     *
     * @return The load-error message displayed to the user.
     */
    public String printLoadError() {
        return printMessage(ERROR_LOAD);
    }

    /**
     * Displays an error for an individual task that cannot be loaded.
     *
     * @return The task-load-error message displayed to the user.
     */
    public String printLoadTaskError() {
        return printMessage(ERROR_LOAD_TASK);
    }

    /**
     * Displays an error for an unknown command keyword.
     *
     * @return The invalid-keyword error message displayed to the user.
     */
    public String printKeywordInvalidError() {
        return printMessage(ERROR_KEYWORD_INVALID);
    }

    /**
     * Prints a message to standard output, appended with a message line.
     *
     * @param msg The message to be printed.
     *
     * @return The message printed
     */
    public String printMessage(String msg) {
        System.out.print(msg + "\n" + MESSAGE_LINE);
        return msg;
    }
}
