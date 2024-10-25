package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the amount donated by a donor in thousands of dollars (k dollar).
 * Guarantees: immutable; is valid as declared in {@link #isValidAmount(String)}
 */
public class DonatedAmount implements Comparable<DonatedAmount> {

    public static final String MESSAGE_CONSTRAINTS =
            "Donated amount should be a non-negative number with up to two decimal places.";

    /*
     * The validation regex allows only non-negative numbers with up to two decimal places.
     */
    public static final String VALIDATION_REGEX = "\\d+(\\.\\d{1,2})?";

    private final Double amount;

    /**
     * Constructs a {@code DonatedAmount}.
     *
     * @param amount A valid non-negative number representing the donated amount.
     */
    public DonatedAmount(String amount) {
        requireNonNull(amount);
        checkArgument(isValidAmount(amount), MESSAGE_CONSTRAINTS);
        this.amount = Double.parseDouble(amount);
    }

    /**
     * Returns true if a given string is a valid representation of donated amount.
     *
     * @param test The string to test.
     * @return True if the test string matches the validation regex.
     */
    public static boolean isValidAmount(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DonatedAmount)) {
            return false;
        }

        DonatedAmount otherAmount = (DonatedAmount) other;
        return amount.equals(otherAmount.amount);
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }

    @Override
    public String toString() {
        return amount.toString();
    }

    @Override
    public int compareTo(DonatedAmount other) {
        return Double.compare(other.amount, this.amount);
    }
}
