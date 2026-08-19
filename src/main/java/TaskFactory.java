class TaskFactory {
    public static Todo makeTodo(String[] inputTokens) {
        String[] data = parseData(inputTokens, new String[]{});
        return new Todo(data[0]);
    }

    public static Deadline makeDeadline(String[] inputTokens) {
        String[] data = parseData(inputTokens, new String[]{"/by"});
        return new Deadline(data[0], data[1]);
    }

    public static Event makeEvent(String[] inputTokens) {
        String[] data = parseData(inputTokens, new String[]{"/from", "/to"});
        return new Event(data[0], data[1], data[2]);
    }

    private static String[] parseData(String[] tokens, String[] keywords) {
        String[] parsedData = new String[keywords.length + 1];
        int next = 1;
        for (int i = 0; i < keywords.length; i++) {
            StringBuilder sb = new StringBuilder();
            next = joinTokensFromIndexUntil(tokens, next, sb, keywords[i]);
            parsedData[i] = sb.toString();
        }
        StringBuilder sb = new StringBuilder();
        joinTokensFromIndex(tokens, next, sb);
        parsedData[keywords.length] = sb.toString();

        return parsedData;
    }

    private static void joinTokensFromIndex(String[] tokens, int index, StringBuilder sb) {
        while (index < tokens.length) {
            sb.append(tokens[index]);
            sb.append(" ");
            index++;
        }

        sb.deleteCharAt(sb.length() - 1);
    }

    private static int joinTokensFromIndexUntil(String[] tokens, int index, StringBuilder sb, String keyword) {
        while (!tokens[index].equalsIgnoreCase(keyword)) {
            sb.append(tokens[index]);
            sb.append(" ");
            index++;
        }

        sb.deleteCharAt(sb.length() - 1);
        return ++index;
    }
}
