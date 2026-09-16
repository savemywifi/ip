package bern.logic;

import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Function;

import bern.datetime.DateTime;
import bern.datetime.DateTimeFactory;
import bern.storage.SaveDataController;
import bern.task.TaskFactory;
import bern.task.TaskManager;
import bern.ui.Dialog;
import javafx.application.Platform;

/** Processes user commands and coordinates task-management operations */
public class Controller {
    private static final TaskManager TASK_MANAGER = TaskManager.getInstance();
    private static final Dialog DIALOG = Dialog.getInstance();

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
        FIND(Controller::attemptFindTasks),
        SCHEDULE(Controller::attemptShowSchedule);

        private final Function<String[], Response> action;

        Keyword(Function<String[], Response> action) {
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
     * @return A response containing the goodbye or error message displayed to the user.
     */
    private static Response attemptExit(String[] inputTokens) {
        if (inputTokens.length != 1) {
            return DIALOG.printIncorrectKeywordUsageError(Keyword.BYE, Keyword.BYE);
        }
        if (!SaveDataController.saveTaskData(TASK_MANAGER.getTaskList())) {
            return DIALOG.printSaveError();
        }
        Platform.exit();
        return DIALOG.sayGoodbye();
    }

    /**
     * Attempt to list tasks, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return The formatted task list or an error message displayed to the user.
     */
    private static Response attemptListTasks(String[] inputTokens) {
        if (inputTokens.length != 1) {
            return DIALOG.printIncorrectKeywordUsageError(Keyword.LIST, Keyword.LIST);
        }

        return TASK_MANAGER.listTasks();
    }

    /** Returns matching tasks, or a usage error if the command does not contain exactly one search token. */
    private static Response attemptFindTasks(String[] inputTokens) {
        if (inputTokens.length != 2) {
            return DIALOG.printIncorrectKeywordUsageError(
                    Keyword.FIND, Keyword.FIND + " [search token]");
        }

        return TASK_MANAGER.findTasks(inputTokens[1]);
    }

    /** Returns the requested day's schedule, defaulting to today when the command has no arguments. */
    private static Response attemptShowSchedule(String[] inputTokens) {
        try {
            String dateArgument = String.join(" ", Arrays.copyOfRange(inputTokens, 1, inputTokens.length));
            DateTime scheduleDate = inputTokens.length == 1
                    ? DateTime.now()
                    : DateTimeFactory.parseDateTime(dateArgument);
            return TASK_MANAGER.showSchedule(scheduleDate);
        } catch (DateTimeParseException e) {
            return DIALOG.printInvalidDateTimeError(e.getParsedString());
        }
    }

    /**
     * Attempt to extract a task number from the second argument. Assumes only two tokens are given and rejects all
     * other inputs
     *
     * @param inputTokens The tokens in the input
     *
     * @return The valid one-based task number.
     * @throws IllegalArgumentException If the input is invalid or no tasks exist.
     */
    private static int tryGetTaskNumber(String[] inputTokens) throws IllegalArgumentException {
        if (!TASK_MANAGER.hasTasks()) {
            throw new IllegalArgumentException(DIALOG.printNoTasksError().toString());
        }

        if (inputTokens.length != 2) {
            throw new IllegalArgumentException(
                    DIALOG.printIncorrectKeywordUsageError(inputTokens[0], inputTokens[0]).toString());
        }

        int taskNumber;

        try {
            taskNumber = Integer.parseInt(inputTokens[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(DIALOG.printInvalidTaskNumberError(TASK_MANAGER.size()).toString());
        }

        if (taskNumber < 1 || taskNumber > TASK_MANAGER.size()) {
            throw new IllegalArgumentException(DIALOG.printInvalidTaskNumberError(TASK_MANAGER.size()).toString());
        }

        return taskNumber;
    }

    /**
     * Attempt to mark a task as done, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     * @return The confirmation or error message displayed to the user.
     */
    private static Response attemptMarkTask(String[] inputTokens) {
        try {
            int taskNumber = tryGetTaskNumber(inputTokens);
            return TASK_MANAGER.markTask(taskNumber);
        } catch (IllegalArgumentException e) {
            return DIALOG.printMessage(e.getMessage());
        }
    }

    /**
     * Attempt to mark a task as undone, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return The confirmation or error message displayed to the user.
     */
    private static Response attemptUnmarkTask(String[] inputTokens) {
        try {
            int taskNumber = tryGetTaskNumber(inputTokens);
            return TASK_MANAGER.unmarkTask(taskNumber);
        } catch (IllegalArgumentException e) {
            return DIALOG.printMessage(e.getMessage());
        }
    }

    /**
     * Attempt to make and add a todo, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return The confirmation or error message displayed to the user.
     */
    private static Response attemptMakeTodo(String[] inputTokens) {
        try {
            return TASK_MANAGER.addTask(TaskFactory.makeTodo(inputTokens));
        } catch (ParseException e) {
            return DIALOG.printMessage(getParseExceptionResponse(e) + "Command syntax: todo <task name>");
        }
    }

    /**
     * Attempt to make and add a deadline, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return The confirmation or error message displayed to the user.
     */
    private static Response attemptMakeDeadline(String[] inputTokens) {
        try {
            return TASK_MANAGER.addTask(TaskFactory.makeDeadline(inputTokens));
        } catch (ParseException e) {
            return DIALOG.printMessage(getParseExceptionResponse(e)
                    + "Command syntax: deadline <task name> /by <due date>");
        } catch (DateTimeParseException e) {
            return DIALOG.printInvalidDateTimeError(e.getParsedString());
        }
    }

    /**
     * Attempt to make and add an event, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return The confirmation or error message displayed to the user.
     */
    private static Response attemptMakeEvent(String[] inputTokens) {
        try {
            return TASK_MANAGER.addTask(TaskFactory.makeEvent(inputTokens));
        } catch (ParseException e) {
            return DIALOG.printMessage(getParseExceptionResponse(e)
                    + "Command syntax: event <task name> /from <start date time> /to <end date time>");
        } catch (DateTimeParseException e) {
            return DIALOG.printInvalidDateTimeError(e.getParsedString());
        } catch (IllegalArgumentException e) {
            return DIALOG.printMessage(e.getMessage());
        }
    }

    /**
     * Attempt to delete a task, or print an error message otherwise
     *
     * @param inputTokens The tokens in the input
     *
     * @return The confirmation or error message displayed to the user.
     */
    private static Response attemptDeleteTask(String[] inputTokens) {
        try {
            int taskNumber = tryGetTaskNumber(inputTokens);
            return TASK_MANAGER.deleteTask(taskNumber);
        } catch (IllegalArgumentException e) {
            return DIALOG.printMessage(e.getMessage());
        }
    }

    /**
     * Handles a user command and returns the corresponding response.
     *
     * @param input The command entered by the user.
     * @return The command result, or a message explaining invalid input.
     */
    public Response getResponse(String input) {
        if (input.isBlank()) {
            return DIALOG.printKeywordInvalidError();
        }

        String[] inputTokens = input.strip().split(" ");
        try {
            Keyword keyword = Keyword.valueOf(inputTokens[0].toUpperCase());
            return keyword.action.apply(inputTokens);
        } catch (IllegalArgumentException e) {
            return DIALOG.printKeywordInvalidError();
        }
    }

    /**
     * Loads saved tasks into the initially empty task manager.
     *
     * @return Whether at least one task was loaded.
     */
    public boolean loadTasks() {
        return TASK_MANAGER.loadTaskList(SaveDataController.readTaskData());
    }

    /** Starts the command-line application. */
    static void main() {
        DIALOG.greetUser();

        Scanner sc = new Scanner(System.in);
        boolean isReadingInput = true;
        Keyword keyword;

        TASK_MANAGER.loadTaskList(SaveDataController.readTaskData());

        if (TASK_MANAGER.hasTasks()) {
            DIALOG.printTasksLoaded();
        }

        while (isReadingInput) {
            String input = DIALOG.promptForInput(sc);
            String[] inputTokens = input.split(" ");

            try {
                keyword = Keyword.valueOf(inputTokens[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                DIALOG.printKeywordInvalidError();
                continue;
            }

            keyword.action.apply(inputTokens);

            // Manual check for Keyword.BYE
            if (keyword.equals(Keyword.BYE) && inputTokens.length == 1) {
                isReadingInput = false;
            }
        }

        sc.close();
        DIALOG.sayGoodbye();
    }
}
