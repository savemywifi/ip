package bern;

import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.function.Consumer;

import bern.storage.SaveDataController;
import bern.task.TaskFactory;
import bern.task.TaskManager;
import bern.ui.Ui;

/** Runs the Bern task management application. */
public class Bern {
    /** Stores the commands supported by the application and their handlers. */
    private enum Keyword {
        BYE(Bern::attemptExit),
        LIST(Bern::attemptListTasks),
        MARK(Bern::attemptMarkTask),
        UNMARK(Bern::attemptUnmarkTask),
        TODO(Bern::attemptMakeTodo),
        DEADLINE(Bern::attemptMakeDeadline),
        EVENT(Bern::attemptMakeEvent),
        DELETE(Bern::attemptDeleteTask),
        FIND(Bern::attemptFindTasks);

        private final Consumer<String[]> action;

        Keyword(Consumer<String[]> action) {
            this.action = action;
        }
    }

    /**
     * Takes the parse exception passed from bern.task.TaskFactory and parses it into an appropriate error message
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
            Ui.getInstance().printIncorrectKeywordUsageError(Keyword.BYE, Keyword.BYE);
            return false;
        }
        if (!SaveDataController.saveTaskData(TaskManager.getInstance().getTaskList())) {
            Ui.getInstance().printSaveError();
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
            Ui.getInstance().printIncorrectKeywordUsageError(Keyword.LIST, Keyword.LIST);
            return false;
        }

        Ui.getInstance().printMessage(TaskManager.getInstance().listTasks());
        return true;
    }

    private static boolean attemptFindTasks(String[] inputTokens) {
        if (inputTokens.length != 2) {
            Ui.getInstance().printIncorrectKeywordUsageError(Keyword.FIND, Keyword.FIND.toString() + " [search token]");
            return false;
        }

        Ui.getInstance().printMessage(TaskManager.getInstance().findTasks(inputTokens[1]));
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
        if (!TaskManager.getInstance().hasTasks()) {
            Ui.getInstance().printNoTasksError();
            return -1;
        }

        if (inputTokens.length != 2) {
            Ui.getInstance().printIncorrectKeywordUsageError(inputTokens[0], inputTokens[0]);
            return -1;
        }

        int taskIndex;

        try {
            taskIndex = Integer.parseInt(inputTokens[1]);
        } catch (NumberFormatException e) {
            Ui.getInstance().printInvalidTaskNumberError();
            return -1;
        }

        if (taskIndex < 1 || taskIndex > TaskManager.getInstance().size()) {
            Ui.getInstance().printInvalidTaskNumberError();
            return -1;
        }

        return taskIndex;
    }

    /**
     * Attempt to mark a task as done, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     */
    private static void attemptMarkTask(String[] inputTokens) {
        int taskNumber = tryGetTaskNumber(inputTokens);

        if (taskNumber == -1) {
            return;
        }

        Ui.getInstance().printMessage(TaskManager.getInstance().markTask(taskNumber));
    }

    /**
     * Attempt to mark a task as undone, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     */
    private static void attemptUnmarkTask(String[] inputTokens) {
        int taskNumber = tryGetTaskNumber(inputTokens);
        if (taskNumber == -1) {
            return;
        }

        Ui.getInstance().printMessage(TaskManager.getInstance().unmarkTask(taskNumber));
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
            Ui.getInstance().printMessage(TaskManager.getInstance().addTask(TaskFactory.makeTodo(inputTokens)));
        } catch (ParseException e) {
            //TODO: parseException responses
            Ui.getInstance().printMessage(getParseExceptionResponse(e) + "Command syntax: todo <task name>");
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
            Ui.getInstance().printMessage(TaskManager.getInstance().addTask(TaskFactory.makeDeadline(inputTokens)));
        } catch (ParseException e) {
            Ui.getInstance().printMessage(getParseExceptionResponse(e)
                    + "Command syntax: deadline <task name> /by <due date>");
            return false;
        } catch (DateTimeParseException e) {
            // TODO: improve error messages
            Ui.getInstance().printInvalidDateTimeError(e.getParsedString());
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
            Ui.getInstance().printMessage(TaskManager.getInstance().addTask(TaskFactory.makeEvent(inputTokens)));
        } catch (ParseException e) {
            //TODO: this one
            Ui.getInstance().printMessage(getParseExceptionResponse(e)
                    + "Command syntax: event <task name> /from <start date time> /to <end date time>");
            return false;
        } catch (DateTimeParseException e) {
            // TODO: improve error messages
            Ui.getInstance().printInvalidDateTimeError(e.getParsedString());
            return false;
        } catch (IllegalArgumentException e) {
            Ui.getInstance().printMessage(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Attempt to delete a task, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     */
    private static void attemptDeleteTask(String[] inputTokens) {
        int taskNumber = tryGetTaskNumber(inputTokens);
        if (taskNumber == -1) {
            return;
        }

        Ui.getInstance().printMessage(TaskManager.getInstance().deleteTask(taskNumber));
    }

    /** Starts the command-line application. */
    public static void main(String[] args) {
        Ui.getInstance().greetUser();

        Scanner sc = new Scanner(System.in);
        boolean isReadingInput = true;
        Keyword keyword;

        TaskManager.getInstance().loadTaskList(SaveDataController.readTaskData());

        if (TaskManager.getInstance().hasTasks()) {
            Ui.getInstance().printLoadedTasks();
        }

        while (isReadingInput) {
            String input = Ui.getInstance().promptForInput(sc);
            String[] inputTokens = input.split(" ");

            try {
                keyword = Keyword.valueOf(inputTokens[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                Ui.getInstance().printKeywordInvalidError();
                continue;
            }

            keyword.action.accept(inputTokens);

            // Manual check for Keyword.BYE
            if (keyword.equals(Keyword.BYE) && inputTokens.length == 1) {
                isReadingInput = false;
            }
        }

        sc.close();
        Ui.getInstance().sayGoodbye();
    }
}
