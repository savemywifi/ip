package bern.datetime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTime implements Comparable<DateTime> {
    static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh.mma");
    static final String separator = " @ ";

    private final LocalDate localDate;
    private final LocalTime localTime;

    DateTime(LocalDate localDate, LocalTime localTime) {
        this.localDate = localDate;
        this.localTime = localTime;
    }

    @Override
    public String toString() {
        if (this.localTime == null) {
            return this.localDate.format(dateFormatter);
        }

        return String.format("%s%s%s", localDate.format(dateFormatter), separator, localTime.format(timeFormatter));
    }

    /**
     * Compares two DateTimes. Returns a negative integer if the other date/time is after the current date/time, 0 if
     * they are equal, or a positive integer if the other date/time. is before the current date/time. If one object has
     * no specified time value (it is null), that time will be "before" the DateTime with a specified time field if
     * they contain the same date.
     *
     * @param other the object to be compared.
     * @return An integer indicating which DateTime object is greater.
     */
    @Override
    public int compareTo(DateTime other) {
        int dayDifference = (int) ChronoUnit.DAYS.between(other.localDate, this.localDate);
        if (dayDifference != 0) {
            return dayDifference;
        }

        if (this.localTime == null && other.localTime == null) {
            return 0;
        } else if (this.localTime == null) {
            return -1;
        } else if (other.localTime == null) {
            return 1;
        }

        return (int) ChronoUnit.MINUTES.between(other.localTime, this.localTime);
    }
}
