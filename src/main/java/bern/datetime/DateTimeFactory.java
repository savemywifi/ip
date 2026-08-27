package bern.datetime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DateTimeFactory {
    LocalDate date;
    LocalTime time;

    private static final String[] hourFormats = {"h","k"};
    private static final String[] minuteFormats = {"m"};
    private static final String[] timeSeparators = {":", "."};

    private static final String[] monthFormatsShort = {"M", "MM"};
    private static final String[] monthFormatsLong = {"MMM", "MMMM"};
    private static final String[] yearFormats = {"y"}; // TODO: accommodate for "yy"
    private static final String[] dateSeparatorsShort = {"/", ".", "-"};

    private static final List<String> dateFormats = DateTimeFactory.generateDateFormats();
    private static final List<String> timeFormats = List.of(
            "ha", // 5pm
            "h:ma", "h.ma", // 5:29pm, 5.29pm
            "km", "k:m", "k.m" // 1729, 17:29, 17.29
    );

    public static DateTime parseDateTime(String dateTimeString) throws DateTimeParseException {
        String[] tokens = dateTimeString.split(" ");
        LocalTime lt = null;
        LocalDate ld = null;

        if (isTime(dateTimeString)) {
            lt = parseTime(dateTimeString);
            ld = LocalDate.now();
        } else if (isTime(tokens[0])) {
            lt = parseTime(tokens[0]);
            ld = parseDate(String.join(" ", Arrays.stream(tokens, 1, tokens.length).toList()));
        } else if (isTime(tokens[tokens.length - 1])) {
            lt = parseTime(tokens[tokens.length - 1]);
            ld = parseDate(String.join(" ", Arrays.stream(tokens, 0, tokens.length - 1).toList()));
        } else {
            ld = parseDate(dateTimeString);
        }

        return new DateTime(ld, lt);
    }

    public static DateTime makeDateFromDataString(String dataString) {
        String[] tokens = dataString.split(DateTime.separator);

        if (tokens.length != 2) {
            throw new IllegalArgumentException("Data has insufficient arguments for date and time");
        }

        return new DateTime(parseDate(tokens[0]), parseTime(tokens[1]));
    }

    private static LocalDate parseDate(String dateString) throws DateTimeParseException {
        LocalDate date = null;
        DateTimeParseException firstSeenException = null;
        for (String format : dateFormats) {
            try {
                date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(format));
                break;
            } catch (DateTimeParseException e) {
                firstSeenException = firstSeenException == null ? e : firstSeenException;
            }
        }

        if (date == null) {
            throw firstSeenException;
        }

        return date;
    }

    private static LocalTime parseTime(String timeString) throws DateTimeParseException {
        LocalTime time = null;
        DateTimeParseException firstSeenException = null;
        for (String format : timeFormats) {
            try {
                time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern(format));
                break;
            } catch (DateTimeParseException e) {
                firstSeenException = firstSeenException == null ? e : firstSeenException;
            }
        }

        if (time == null) {
            throw firstSeenException;
        }

        return time;
    }

    private static boolean isTime(String timeString) {
        for (String format: timeFormats) {
            try {
                LocalTime.parse(timeString, DateTimeFormatter.ofPattern(format));
                return true;
            } catch (DateTimeParseException e) {
                continue;
            }
        }
        return false;
    }

    private static ArrayList<String> generateDateFormats() {
        ArrayList<String> formats = new ArrayList<>();
        for (String yearFormat : yearFormats) {
            for (String monthFormat : monthFormatsShort) {
                for (String dateSeparator : dateSeparatorsShort) {
                    // Little-Endian
                    formats.add(String.format("%s%s%s%s%s",
                            "d",
                            dateSeparator,
                            monthFormat,
                            dateSeparator,
                            yearFormat
                    ));
                    // Big-Endian
                    formats.add(String.format("%s%s%s%s%s",
                            yearFormat,
                            dateSeparator,
                            monthFormat,
                            dateSeparator,
                            "d"
                    ));
                    // Middle-Endian
                    // TODO
                }
            }

            for (String monthFormat : monthFormatsLong) {
                // Little-Endian
                formats.add(String.format("%s %s %s",
                        "d",
                        monthFormat,
                        yearFormat
                ));

                // Big-Endian
                formats.add(String.format("%s %s %s",
                        yearFormat,
                        monthFormat,
                        "d"
                ));

                // Middle-Endian
                formats.add(String.format("%s %s %s",
                        monthFormat,
                        "d",
                        yearFormat
                ));
            }
        }
        return formats;
    }
}