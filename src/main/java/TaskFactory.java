import java.text.ParseException;
import java.util.NoSuchElementException;

class TaskFactory {
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
                task = new Deadline(data[2], data[3]);
                break;
            case "E":
                if (data.length != 5) {
                    throw new IllegalArgumentException("Stored event has incorrect number of arguments");
                }
                task = new Event(data[2], data[3], data[4]);
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

    public static Deadline makeDeadline(String[] inputTokens) throws ParseException {
        String[] data = parseData(inputTokens, new String[]{"/by"});
        return new Deadline(data[0], data[1]);
    }

    public static Event makeEvent(String[] inputTokens) throws ParseException {
        String[] data = parseData(inputTokens, new String[]{"/from", "/to"});
        return new Event(data[0], data[1], data[2]);
    }

    private static String[] parseData(String[] tokens, String[] keywords) throws ParseException {
        String[] parsedData = new String[keywords.length + 1];
        int next = 1;
        for (int i = 0; i < keywords.length; i++) {
            StringBuilder sb = new StringBuilder();
            try {
                next = joinTokensFromIndexUntil(tokens, next, sb, keywords[i]);
            } catch (NoSuchElementException e) {
                // current keyword not found in order
                throw new ParseException(keywords[i], i);
            }
            parsedData[i] = sb.toString().strip();
            if (parsedData[i].isEmpty()) {
                // parsed argument is empty
                throw new ParseException(i == 0 ? tokens[0] : keywords[i-1], -1);
            }
        }
        StringBuilder sb = new StringBuilder();
        joinTokensFromIndex(tokens, next, sb);
        parsedData[keywords.length] = sb.toString().strip();

        if (parsedData[keywords.length].isEmpty()) {
            // parsed argument is empty
            throw new ParseException(keywords.length == 0 ? tokens[0] : keywords[keywords.length - 1], -1);
        }

        return parsedData;
    }

    private static void joinTokensFromIndex(String[] tokens, int index, StringBuilder sb) {
        while (index < tokens.length) {
            sb.append(tokens[index]);
            sb.append(" ");
            index++;
        }
    }

    private static int joinTokensFromIndexUntil(
            String[] tokens, int index, StringBuilder sb, String keyword) throws NoSuchElementException {
        if (index == tokens.length) {
            throw new NoSuchElementException();
        }

        while (!tokens[index].equalsIgnoreCase(keyword)) {
            sb.append(tokens[index]);
            sb.append(" ");
            index++;

            if (index == tokens.length) {
                throw new NoSuchElementException();
            }
        }

        return ++index;
    }
}
