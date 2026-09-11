package bern.logic;

import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
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
    private static final TaskManager taskManager = TaskManager.getInstance();
    private static final Dialog dialog = Dialog.getInstance();

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
            return dialog.printIncorrectKeywordUsageError(Keyword.BYE, Keyword.BYE);
        }
        if (!SaveDataController.saveTaskData(taskManager.getTaskList())) {
            return dialog.printSaveError();
        }
        Platform.exit();
        return dialog.sayGoodbye();
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
            return dialog.printIncorrectKeywordUsageError(Keyword.LIST, Keyword.LIST);
        }

        return taskManager.listTasks();
    }

    private static Response attemptFindTasks(String[] inputTokens) {
        if (inputTokens.length != 2) {
            return dialog.printIncorrectKeywordUsageError(
                    Keyword.FIND, Keyword.FIND + " [search token]");
        }

        return taskManager.findTasks(inputTokens[1]);
    }

    private static Response attemptShowSchedule(String[] inputTokens) {
        DateTime scheduleDate;
        try {
            scheduleDate = inputTokens.length == 1
                    ? DateTime.now()
                    : DateTimeFactory.parseDateTime(String.join(" ", inputTokens).split(" ", 2)[1]);
            return taskManager.showSchedule(scheduleDate);
        } catch (DateTimeParseException e) {
            return dialog.printInvalidDateTimeError(e.getParsedString());
        } catch (NoSuchElementException e) {
            return dialog.printNoTasksError();
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
        if (!taskManager.hasTasks()) {
            throw new IllegalArgumentException(dialog.printNoTasksError().toString());
        }

        if (inputTokens.length != 2) {
            throw new IllegalArgumentException(
                    dialog.printIncorrectKeywordUsageError(inputTokens[0], inputTokens[0]).toString());
        }

        int taskIndex;

        try {
            taskIndex = Integer.parseInt(inputTokens[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(dialog.printInvalidTaskNumberError(taskManager.size()).toString());
        }

        if (taskIndex < 1 || taskIndex > taskManager.size()) {
            throw new IllegalArgumentException(dialog.printInvalidTaskNumberError(taskManager.size()).toString());
        }

        return taskIndex;
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
            return dialog.printMessage(taskManager.markTask(taskNumber));
        } catch (IllegalArgumentException e) {
            return dialog.printMessage(e.getMessage());
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
            return dialog.printMessage(taskManager.unmarkTask(taskNumber));
        } catch (IllegalArgumentException e) {
            return dialog.printMessage(e.getMessage());
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
            return dialog.printMessage(taskManager.addTask(TaskFactory.makeTodo(inputTokens)));
        } catch (ParseException e) {
            return dialog.printMessage(getParseExceptionResponse(e) + "Command syntax: todo <task name>");
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
            return dialog.printMessage(taskManager.addTask(TaskFactory.makeDeadline(inputTokens)));
        } catch (ParseException e) {
            return dialog.printMessage(getParseExceptionResponse(e)
                    + "Command syntax: deadline <task name> /by <due date>");
        } catch (DateTimeParseException e) {
            return dialog.printInvalidDateTimeError(e.getParsedString());
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
            return dialog.printMessage(
                    taskManager.addTask(TaskFactory.makeEvent(inputTokens)));
        } catch (ParseException e) {
            return dialog.printMessage(getParseExceptionResponse(e)
                    + "Command syntax: event <task name> /from <start date time> /to <end date time>");
        } catch (DateTimeParseException e) {
            return dialog.printInvalidDateTimeError(e.getParsedString());
        } catch (IllegalArgumentException e) {
            return dialog.printMessage(e.getMessage());
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
            return dialog.printMessage(taskManager.deleteTask(taskNumber));
        } catch (IllegalArgumentException e) {
            return dialog.printMessage(e.getMessage());
        }
    }

    public Response getResponse(String input) {
        String[] inputTokens = input.split(" ");
        try {
            Keyword keyword = Keyword.valueOf(inputTokens[0].toUpperCase());
            return keyword.action.apply(inputTokens);
        } catch (IllegalArgumentException e) {
            return dialog.printKeywordInvalidError();
        }
    }

    public boolean loadTasks() {
        return taskManager.loadTaskList(SaveDataController.readTaskData());
    }

    /** Starts the command-line application. */
    static void main() {
        dialog.greetUser();

        Scanner sc = new Scanner(System.in);
        boolean isReadingInput = true;
        Keyword keyword;

        taskManager.loadTaskList(SaveDataController.readTaskData());

        if (taskManager.hasTasks()) {
            dialog.printTasksLoaded();
        }

        while (isReadingInput) {
            String input = dialog.promptForInput(sc);
            String[] inputTokens = input.split(" ");

            try {
                keyword = Keyword.valueOf(inputTokens[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                dialog.printKeywordInvalidError();
                continue;
            }

            keyword.action.apply(inputTokens);

            // Manual check for Keyword.BYE
            if (keyword.equals(Keyword.BYE) && inputTokens.length == 1) {
                isReadingInput = false;
            }
        }

        sc.close();
        dialog.sayGoodbye();
    }
}
