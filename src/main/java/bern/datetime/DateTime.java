package bern.datetime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Stores a given date, and optionally, a given time. A <code>DateTime</code> object corresponds to a date and time
 * represented using Java's LocalDate and LocalTime classes e.g. 28 August 2026 @ 12.24pm
 */
public class DateTime implements Comparable<DateTime> {
    /** Formats dates for display and storage. */
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    /** Formats times for display and storage. */
    static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh.mma");

    /** Separates the date and time parts in stored date-time values. */
    static final String SEPARATOR = " @ ";

    private final LocalDate localDate;
    private final LocalTime localTime;

    /**
     * A constructor for the DateTime class
     *
     * @param localDate The date stored
     * @param localTime The time stored, or null
     */
    DateTime(LocalDate localDate, LocalTime localTime) {
        this.localDate = localDate;
        this.localTime = localTime;
    }

    /**
     * Returns a DateTime of the current date and time.
     *
     * @return A DateTime containing the current date and time.
     */
    public static DateTime now() {
        return new DateTime(LocalDate.now(), LocalTime.now());
    }

    /**
     * A string representation of the DateTime
     *
     * @return The formatted date, optionally followed by the formatted time when one is present
     */
    @Override
    public String toString() {
        if (localTime == null) {
            return localDate.format(DATE_FORMATTER);
        }

        return String.format("%s%s%s", localDate.format(DATE_FORMATTER), SEPARATOR,
                localTime.format(TIME_FORMATTER));
    }

    /**
     * Compares two DateTimes. Returns a negative integer if the other date/time is after the current date/time, 0 if
     * they are equal, or a positive integer if the other date/time is before the current date/time. If one object has
     * no specified time value (it is null), that time will be "before" the DateTime with a specified time field if
     * they contain the same date.
     *
     * @param other the object to be compared.
     * @return An integer indicating which DateTime object is greater.
     */
    @Override
    public int compareTo(DateTime other) {
        int dayDifference = (int) ChronoUnit.DAYS.between(other.localDate, localDate);
        if (dayDifference != 0) {
            return dayDifference;
        }

        if (localTime == null && other.localTime == null) {
            return 0;
        } else if (localTime == null) {
            return -1;
        } else if (other.localTime == null) {
            return 1;
        }

        return (int) ChronoUnit.MINUTES.between(other.localTime, localTime);
    }


    public int compareDate(DateTime other) {
        return (int) ChronoUnit.DAYS.between(other.localDate, localDate);
    }
}
