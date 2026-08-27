package bern.task;

import bern.datetime.DateTime;
import bern.datetime.DateTimeFactory;
import bern.parser.ParseFactory;

import java.text.ParseException;
import java.time.format.DateTimeParseException;

public class TaskFactory extends ParseFactory {
    public static Task makeTaskFromData(String[] data) throws ParseException, IllegalArgumentException {
        Task task = null;
        if (data.length < 3) {
            throw new IllegalArgumentException("Data has insufficient arguments for type, completeness and name");
        }

        switch (data[0]) {
            case "T":
                if (data.length != 3) {
                    throw new IllegalArgumentException("Stored todo has incorrect number of arguments");
                }
                task = new Todo(data[2]);
                break;
            case "D":
                if (data.length != 4) {
                    throw new IllegalArgumentException("Stored deadline has incorrect number of arguments");
                }
                task = new Deadline(data[2], DateTimeFactory.makeDateFromDataString(data[3]));
                break;
            case "E":
                if (data.length != 5) {
                    throw new IllegalArgumentException("Stored event has incorrect number of arguments");
                }
                task = new Event(data[2],
                        DateTimeFactory.makeDateFromDataString(data[3]),
                        DateTimeFactory.makeDateFromDataString(data[4])
                );
                break;
            default:
                throw new ParseException("Invalid task type", -1);
        }
        if (data[1].equals("1")) {
            task.setDone(true);
        } else if (!data[1].equals("0")) {
            throw new IllegalArgumentException("Stored task has invalid completeness marking");
        }

        return task;
    }

    public static Todo makeTodo(String[] inputTokens) throws ParseException {
        String[] data = parseData(inputTokens, new String[]{});
        return new Todo(data[0]);
    }

    public static Deadline makeDeadline(String[] inputTokens) throws ParseException, DateTimeParseException {
        String[] data = parseData(inputTokens, new String[]{"/by"});
        return new Deadline(data[0], DateTimeFactory.parseDateTime(data[1]));
    }

    public static Event makeEvent(String[] inputTokens) throws ParseException, DateTimeParseException, IllegalArgumentException {
        String[] data = parseData(inputTokens, new String[]{"/from", "/to"});

        DateTime startDateTime = DateTimeFactory.parseDateTime(data[1]);
        DateTime endDateTime = DateTimeFactory.parseDateTime(data[2]);

        // Date is wrong
        if (startDateTime.compareTo(endDateTime) >= 0) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        return new Event(data[0], startDateTime, endDateTime);
    }
}
