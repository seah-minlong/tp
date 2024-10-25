package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the number of hours a volunteer has contributed.
 * Guarantees: immutable; is valid as declared in {@link #isValidHours(String)}
 */
public class Hours implements Comparable<Hours> {

    public static final String MESSAGE_CONSTRAINTS =
            "Hour numbers should be an non-negative integer number.";

    /*
     * The validation regex allows only non-negative integer numbers.
     */
    public static final String VALIDATION_REGEX = "\\d+";

    private final Integer hours;

    /**
     * Constructs an {@code Hours}.
     *
     * @param hours A valid non-negative integer representing the number of hours.
     */
    public Hours(String hours) {
        requireNonNull(hours);
        checkArgument(isValidHours(hours), MESSAGE_CONSTRAINTS);
        this.hours = Integer.parseInt(hours);
    }

    /**
     * Returns true if a given string is a valid representation of hours.
     *
     * @param test The string to test.
     * @return True if the test string matches the validation regex.
     */
    public static boolean isValidHours(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public int compareTo(Hours hours) {
        return -this.hours.compareTo(hours.hours);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Hours)) {
            return false;
        }

        Hours otherHours = (Hours) other;
        return hours.equals(otherHours.hours);
    }

    @Override
    public int hashCode() {
        return hours.hashCode();
    }

    @Override
    public String toString() {
        return hours.toString();
    }
}
