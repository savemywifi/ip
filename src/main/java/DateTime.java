import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

class DateTime implements Comparable<DateTime> {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh.mma");
    static final String separator = " @ ";

    private final LocalDate localDate;
    private final LocalTime localTime;

    public DateTime(LocalDate localDate, LocalTime localTime) {
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

    @Override
    public int compareTo(DateTime other) {
        int dayDifference = (int) ChronoUnit.DAYS.between(other.localDate, this.localDate);
        if (dayDifference != 0) {
            return dayDifference;
        }

        return (int) ChronoUnit.MINUTES.between(other.localTime, this.localTime);
    }
}
