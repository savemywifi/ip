package bern;

import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.function.Function;

import bern.storage.SaveDataController;
import bern.task.TaskFactory;
import bern.task.TaskManager;
import bern.ui.Dialog;
import javafx.application.Platform;

/** Runs the Bern task management application. */
public class Controller {
    /** Stores the commands supported by the application and their handlers. */
    private enum Keyword {
        BYE(Controller::attemptExit),
        LIST(Controller::attemptListTasks),
        MARK(Controller::attemptMarkTask),
        UNMARK(Controller::attemptUnmarkTask),
        TODO(Controller::attemptMakeTodo),
        DEADLINE(Controller::attemptMakeDeadline),
        EVENT(Controller::attemptMakeEvent),
        DELETE(Controller::attemptDeleteTask),
        FIND(Controller::attemptFindTasks);

        private final Function<String[], String> action;

        Keyword(Function<String[], String> action) {
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
    private static String attemptExit(String[] inputTokens) {
        if (inputTokens.length != 1) {
            return Dialog.getInstance().printIncorrectKeywordUsageError(Keyword.BYE, Keyword.BYE);
        }
        if (!SaveDataController.saveTaskData(TaskManager.getInstance().getTaskList())) {
            return Dialog.getInstance().printSaveError();
        }
        Platform.exit();
        return Dialog.getInstance().sayGoodbye();
    }

    /**
     * Attempt to list tasks, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return A boolean indicating the success of the operation
     */
    private static String attemptListTasks(String[] inputTokens) {
        if (inputTokens.length != 1) {
            return Dialog.getInstance().printIncorrectKeywordUsageError(Keyword.LIST, Keyword.LIST);
        }

        return Dialog.getInstance().printMessage(TaskManager.getInstance().listTasks());
    }

    private static String attemptFindTasks(String[] inputTokens) {
        if (inputTokens.length != 2) {
            return Dialog.getInstance().printIncorrectKeywordUsageError(
                    Keyword.FIND, Keyword.FIND + " [search token]");
        }

        return Dialog.getInstance().printMessage(
                TaskManager.getInstance().findTasks(inputTokens[1]));
    }

    /**
     * Attempt to extract a task number from the second argument. Assumes only two tokens are given and rejects all
     * other inputs
     *
     * @param inputTokens The tokens in the input
     *
     * @return An integer with the task number, or -1 if the number is invalid
     */
    private static int tryGetTaskNumber(String[] inputTokens) throws IllegalArgumentException {
        if (!TaskManager.getInstance().hasTasks()) {
            throw new IllegalArgumentException(Dialog.getInstance().printNoTasksError());
        }

        if (inputTokens.length != 2) {
            throw new IllegalArgumentException(
                    Dialog.getInstance().printIncorrectKeywordUsageError(inputTokens[0], inputTokens[0]));
        }

        int taskIndex;

        try {
            taskIndex = Integer.parseInt(inputTokens[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(Dialog.getInstance().printInvalidTaskNumberError());
        }

        if (taskIndex < 1 || taskIndex > TaskManager.getInstance().size()) {
            throw new IllegalArgumentException(Dialog.getInstance().printInvalidTaskNumberError());
        }

        return taskIndex;
    }

    /**
     * Attempt to mark a task as done, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     */
    private static String attemptMarkTask(String[] inputTokens) {
        try {
            int taskNumber = tryGetTaskNumber(inputTokens);
            return Dialog.getInstance().printMessage(TaskManager.getInstance().markTask(taskNumber));
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    /**
     * Attempt to mark a task as undone, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     */
    private static String attemptUnmarkTask(String[] inputTokens) {
        try {
            int taskNumber = tryGetTaskNumber(inputTokens);
            return Dialog.getInstance().printMessage(TaskManager.getInstance().unmarkTask(taskNumber));
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    /**
     * Attempt to make and add a todo, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return A boolean indicating the success of this operation
     */
    private static String attemptMakeTodo(String[] inputTokens) {
        try {
            return Dialog.getInstance().printMessage(
                    TaskManager.getInstance().addTask(TaskFactory.makeTodo(inputTokens)));
        } catch (ParseException e) {
            return Dialog.getInstance().printMessage(getParseExceptionResponse(e) + "Command syntax: todo <task name>");
        }
    }

    /**
     * Attempt to make and add a deadline, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return A boolean indicating the success of this operation
     */
    private static String attemptMakeDeadline(String[] inputTokens) {
        try {
            return Dialog.getInstance().printMessage(
                    TaskManager.getInstance().addTask(TaskFactory.makeDeadline(inputTokens)));
        } catch (ParseException e) {
            return Dialog.getInstance().printMessage(getParseExceptionResponse(e)
                    + "Command syntax: deadline <task name> /by <due date>");
        } catch (DateTimeParseException e) {
            return Dialog.getInstance().printInvalidDateTimeError(e.getParsedString());
        }
    }

    /**
     * Attempt to make and add an event, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return A boolean indicating the success of this operation
     */
    private static String attemptMakeEvent(String[] inputTokens) {
        try {
            return Dialog.getInstance().printMessage(
                    TaskManager.getInstance().addTask(TaskFactory.makeEvent(inputTokens)));
        } catch (ParseException e) {
            return Dialog.getInstance().printMessage(getParseExceptionResponse(e)
                    + "Command syntax: event <task name> /from <start date time> /to <end date time>");
        } catch (DateTimeParseException e) {
            return Dialog.getInstance().printInvalidDateTimeError(e.getParsedString());
        } catch (IllegalArgumentException e) {
            return Dialog.getInstance().printMessage(e.getMessage());
        }
    }

    /**
     * Attempt to delete a task, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     */
    private static String attemptDeleteTask(String[] inputTokens) {
        try {
            int taskNumber = tryGetTaskNumber(inputTokens);
            return Dialog.getInstance().printMessage(TaskManager.getInstance().deleteTask(taskNumber));
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    public String getResponse(String input) {
        String[] inputTokens = input.split(" ");
        try {
            Keyword keyword = Keyword.valueOf(inputTokens[0].toUpperCase());
            return keyword.action.apply(inputTokens);
        } catch (IllegalArgumentException e) {
            return Dialog.getInstance().printKeywordInvalidError();
        }
    }

    /** Starts the command-line application. */
    static void main() {
        Dialog.getInstance().greetUser();

        Scanner sc = new Scanner(System.in);
        boolean isReadingInput = true;
        Keyword keyword;

        TaskManager.getInstance().loadTaskList(SaveDataController.readTaskData());

        if (TaskManager.getInstance().hasTasks()) {
            Dialog.getInstance().printLoadedTasks();
        }

        while (isReadingInput) {
            String input = Dialog.getInstance().promptForInput(sc);
            String[] inputTokens = input.split(" ");

            try {
                keyword = Keyword.valueOf(inputTokens[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                Dialog.getInstance().printKeywordInvalidError();
                continue;
            }

            keyword.action.apply(inputTokens);

            // Manual check for Keyword.BYE
            if (keyword.equals(Keyword.BYE) && inputTokens.length == 1) {
                isReadingInput = false;
            }
        }

        sc.close();
        Dialog.getInstance().sayGoodbye();
    }
}
