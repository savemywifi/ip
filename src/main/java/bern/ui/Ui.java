package bern.ui;

import java.util.Scanner;

import bern.task.TaskManager;

/** Handles user-facing input prompts and output messages. */
public class Ui {
    private static Ui instance;

    /** Message line printed after user-facing output. */
    private static final String MESSAGE_LINE = "____________________________________\n";
    private static final String MESSAGE_TASK_LOADED = "Loaded saved tasks. Use list to view them.";
    private static final String MESSAGE_GOODBYE = "> Bye. Hope to see you again soon!";

    /** Banner used to identify the chatbot. */
    private static final String CHATBOT_BANNER = """
             ____                 \s
            |  _ \\                \s
            | |_) | ___ _ __ _ __ \s
            |  _ < / _ \\ '__| '_ \\\s
            | |_) |  __/ |  | | | |
            |____/ \\___|_|  |_| |_|""";

    /** Name displayed for the chatbot. */
    private static final String CHATBOT_NAME = "bern.Bern Tokens";

    /** Template used for the greeting. */
    private static final String TEMPLATE_GREETING = "> Hello! I'm %s. \n"
            + "> What can I do for you?";

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

    private Ui() {
    }

    /**
     * Creates the singleton instance of the Ui class if it doesn't already exist, then returns it.
     *
     * @return An instance of the Ui class
     */
    public static Ui getInstance() {
        if (instance == null) {
            instance = new Ui();
        }

        return instance;
    }

    /**
     * Prompts the user for input, then closes the input with a message line.
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
     */
    public void greetUser() {
        System.out.println(MESSAGE_LINE);
        printMessage(CHATBOT_BANNER);
        printMessage(String.format(TEMPLATE_GREETING, CHATBOT_NAME));
    }

    /**
     * The message displayed to the user if tasks are loaded on startup
     */
    public void printLoadedTasks() {
        printMessage(MESSAGE_TASK_LOADED);
    }

    /**
     * The message displayed when the bot closes
     */
    public void sayGoodbye() {
        printMessage(MESSAGE_GOODBYE);
        System.out.print(MESSAGE_LINE);
    }

    //TODO: reframe to keyword

    /**
     * Displays a message showing correct use of a specified keyword
     *
     * @param keyword A keyword to be used by the user
     * @param expected The expected usage of this keyword
     */
    public void printIncorrectKeywordUsageError(Object keyword, Object expected) {
        printMessage(String.format(ERROR_TEMPLATE_INCORRECT_KEYWORD_USAGE, keyword, expected));
    }

    /**
     * Displays a message showing that there are no tasks
     */
    public void printNoTasksError() {
        printMessage(ERROR_NO_TASKS);
    }

    /**
     * Displays a message showing the range of valid task numbers
     */
    public void printInvalidTaskNumberError() {
        printMessage(String.format(ERROR_TEMPLATE_INVALID_TASK_NUMBER, TaskManager.getInstance().size()));
    }

    /**
     * Displays a message showing the user's invalid date-time input
     *
     * @param invalidDateTime The invalid date-time input given by the user
     */
    public void printInvalidDateTimeError(String invalidDateTime) {
        printMessage(String.format(ERROR_TEMPLATE_INVALID_DATE_TIME, invalidDateTime));
    }

    /** Displays an error for a directory that cannot be created. */
    public void printDirectoryError() {
        printMessage(ERROR_DIRECTORY);
    }

    /** Displays an error for task data that cannot be saved. */
    public void printSaveError() {
        printMessage(ERROR_SAVE);
    }

    /** Displays an error for task data that cannot be loaded. */
    public void printLoadError() {
        printMessage(ERROR_LOAD);
    }

    /** Displays an error for an individual task that cannot be loaded. */
    public void printLoadTaskError() {
        printMessage(ERROR_LOAD_TASK);
    }

    /** Displays an error for an unknown command keyword. */
    public void printKeywordInvalidError() {
        printMessage(ERROR_KEYWORD_INVALID);
    }

    /**
     * Prints a message to standard output, appended with a message line.
     *
     * @param msg The message to be printed.
     */
    public void printMessage(String msg) {
        System.out.print(msg + "\n" + MESSAGE_LINE);
    }
}
