package bern.parser;

import java.text.ParseException;
import java.util.NoSuchElementException;

/**
 * Provides utilities for extracting command arguments separated by specified keywords
 */
public class ParseFactory {
    protected static String[] parseData(String[] tokens, String[] keywords) throws ParseException {
        String[] parsedData = new String[keywords.length + 1];
        int next = 1;
        for (int i = 0; i < keywords.length; i++) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                next = joinTokensFromIndexUntil(tokens, next, stringBuilder, keywords[i]);
            } catch (NoSuchElementException e) {
                // current keyword not found in order
                throw new ParseException(keywords[i], i);
            }
            parsedData[i] = stringBuilder.toString().strip();

            if (parsedData[i].isEmpty()) {
                // parsed argument is empty
                throw new ParseException(i == 0 ? tokens[0] : keywords[i - 1], -1);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        joinTokensFromIndex(tokens, next, stringBuilder);
        parsedData[keywords.length] = stringBuilder.toString().strip();

        if (parsedData[keywords.length].isEmpty()) {
            // parsed argument is empty
            throw new ParseException(keywords.length == 0 ? tokens[0] : keywords[keywords.length - 1], -1);
        }

        return parsedData;
    }

    private static void joinTokensFromIndex(String[] tokens, int index, StringBuilder stringBuilder) {
        while (index < tokens.length) {
            stringBuilder.append(tokens[index]);
            stringBuilder.append(" ");
            index++;
        }
    }

    private static int joinTokensFromIndexUntil(
            String[] tokens, int index, StringBuilder stringBuilder, String keyword) throws NoSuchElementException {
        if (index == tokens.length) {
            throw new NoSuchElementException();
        }

        while (!tokens[index].equalsIgnoreCase(keyword)) {
            stringBuilder.append(tokens[index]);
            stringBuilder.append(" ");
            index++;

            if (index == tokens.length) {
                throw new NoSuchElementException();
            }
        }

        return ++index;
    }
}
