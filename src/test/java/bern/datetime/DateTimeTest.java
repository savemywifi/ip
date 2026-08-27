package bern.datetime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class DateTimeTest {
    @Test
    public void dateTimeString_nullTime_displays() {
        LocalDate date = LocalDate.of(2026, 8, 27);

        DateTime dateTime = new DateTime(date, null);

        assertEquals(
                date.format(DateTime.DATE_FORMATTER),
                dateTime.toString()
        );
    }

    @Test
    public void dateTimeString_nullTime_comparisons() {
        LocalDate date = LocalDate.of(2026, 8, 27);
        LocalTime time = LocalTime.of(6, 7);

        DateTime nullDateTime1 = new DateTime(date, null);
        DateTime nullDateTime2 = new DateTime(date, null);
        DateTime dateTime = new DateTime(date, time);

        assertTrue(dateTime.compareTo(nullDateTime1) > 0);
        assertTrue(nullDateTime1.compareTo(dateTime) < 0);
        assertEquals(0, nullDateTime1.compareTo(nullDateTime2));
    }
}
