package bern.datetime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A factory class for DateTime, used to parse textual inputs into DateTime objects
 */
public class DateTimeFactory {
    private static final String[] HOUR_FORMATS = {"h", "k"};
    private static final String[] MINUTE_FORMATS = {"m"};
    private static final String[] TIME_SEPARATORS = {":", "."};

    private static final String[] MONTH_FORMATS_SHORT = {"M", "MM"};
    private static final String[] MONTH_FORMATS_LONG = {"MMM", "MMMM"};
    private static final String[] YEAR_FORMATS = {"y"}; // TODO: accommodate for "yy"
    private static final String[] DATE_SEPARATORS_SHORT = {"/", ".", "-"};

    private static final List<String> DATE_FORMATS = DateTimeFactory.generateDateFormats();
    private static final List<String> TIME_FORMATS = List.of(
            "ha", // 5pm
            "h:ma", "h.ma", // 5:29pm, 5.29pm
            "km", "k:m", "k.m" // 1729, 17:29, 17.29
    );

    /**
     * Parses a date-time string into a DateTime
     *
     * @param dateTimeString The string to be parsed
     * @return The DateTime matching the given string
     * @throws DateTimeParseException
     */
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

    /**
     * Parses a date-time from a saved datastring
     *
     * @param dataString The datastring to parse a date-time from
     * @return The parsed DateTime
     */
    public static DateTime makeDateFromDataString(String dataString) {
        String[] tokens = dataString.split(DateTime.SEPARATOR);

        if (tokens.length != 2) {
            throw new IllegalArgumentException("Data has insufficient arguments for date and time");
        }

        return new DateTime(parseDate(tokens[0]), parseTime(tokens[1]));
    }

    private static LocalDate parseDate(String dateString) throws DateTimeParseException {
        LocalDate date = null;
        DateTimeParseException firstSeenException = null;
        for (String format : DATE_FORMATS) {
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
        for (String format : TIME_FORMATS) {
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
        for (String format : TIME_FORMATS) {
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
        for (String yearFormat : YEAR_FORMATS) {
            for (String monthFormat : MONTH_FORMATS_SHORT) {
                for (String dateSeparator : DATE_SEPARATORS_SHORT) {
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

            for (String monthFormat : MONTH_FORMATS_LONG) {
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
