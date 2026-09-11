package bern.task;

import bern.datetime.DateTime;

/**
 * An interface for objects that have comparable time periods/instants
 */
public interface IDateTimeComparable {
    DateTime getReferenceDateTime();

    int compareDateTime(IDateTimeComparable timeComparable);
}
