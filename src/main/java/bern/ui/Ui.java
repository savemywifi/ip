package bern.ui;

import bern.task.TaskManager;

import java.util.Scanner;

public class Ui {
    private static Ui instance;

    /** Messages */
    private static final String MESSAGE_LINE = "____________________________________\n";
    private static final String MESSAGE_TASK_LOADED = "Loaded saved tasks. Use list to view them.";
    private static final String MESSAGE_GOODBYE = "> Bye. Hope to see you again soon!";

    /** Constants for chatbot identity */
    private static final String CHATBOT_BANNER = """
             ____                 \s
            |  _ \\                \s
            | |_) | ___ _ __ _ __ \s
            |  _ < / _ \\ '__| '_ \\\s
            | |_) |  __/ |  | | | |
            |____/ \\___|_|  |_| |_|""";

    /** Chatbot name */
    private static final String CHATBOT_NAME = "bern.Bern Tokens";

    /** Templates */
    private static final String TEMPLATE_GREETING = "> Hello! I'm %s. \n"
            + "> What can I do for you?";

    /** Errors */
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

    public static Ui getInstance() {
        if (instance == null) {
            instance = new Ui();
        }

        return instance;
    }

    /**
     * Prompts the user for an input, then closes the input with a message line
     *
     * @param sc Scanner to receive input from
     *
     * @return Received input, stripped of whitespace
     */
    public String promptForInput(Scanner sc) {
        String input = "";

        while (input.isEmpty()) {
            input = sc.nextLine().strip();
        }

        System.out.print(MESSAGE_LINE);
        return input;
    }

    public void greetUser() {
        System.out.println(MESSAGE_LINE);
        printMessage(CHATBOT_BANNER);
        printMessage(String.format(TEMPLATE_GREETING, CHATBOT_NAME));
    }

    public void printLoadedTasks() {
        printMessage(MESSAGE_TASK_LOADED);
    }

    public void sayGoodbye() {
        printMessage(MESSAGE_GOODBYE);
        System.out.print(MESSAGE_LINE);
    }

    //TODO: reframe to keyword
    public void printIncorrectKeywordUsageError(Object keyword, Object expected) {
        printMessage(String.format(ERROR_TEMPLATE_INCORRECT_KEYWORD_USAGE, keyword, expected));
    }

    public void printNoTasksError() {
        printMessage(ERROR_NO_TASKS);
    }

    public void printInvalidTaskNumberError() {
        printMessage(String.format(ERROR_TEMPLATE_INVALID_TASK_NUMBER, TaskManager.getInstance().size()));
    }

    public void printInvalidDateTimeError(String invalidDateTime) {
        printMessage(String.format(ERROR_TEMPLATE_INVALID_DATE_TIME, invalidDateTime));
    }

    public void printDirectoryError() {
        printMessage(ERROR_DIRECTORY);
    }

    public void printSaveError() {
        printMessage(ERROR_SAVE);
    }

    public void printLoadError() {
        printMessage(ERROR_LOAD);
    }

    public void printLoadTaskError() {
        printMessage(ERROR_LOAD_TASK);
    }

    public void printKeywordInvalidError() {
        printMessage(ERROR_KEYWORD_INVALID);
    }

    /**
     * Prints a message to the standard output, appended with a message line
     *
     * @param msg The message to be printed
     */
    public void printMessage(String msg) {
        System.out.print(msg + "\n" + MESSAGE_LINE);
    }
}
