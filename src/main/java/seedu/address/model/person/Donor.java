package seedu.address.model.person;


import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Donor in the address book.
 * Inherits from Person and includes donated amount from the donor.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Donor extends Person implements Comparable<Donor> {

    private final DonatedAmount donatedAmount;

    /**
     * Constructs a {@code Donor}.
     *
     * @param name    Name of the donor.
     * @param phone   Phone number of the donor.
     * @param email   Email address of the donor.
     * @param address Address of the donor.
     * @param tags    Tags associated with the donor.
     * @param donatedAmount   Hours contributed by the donor.
     */
    public Donor(Name name, Phone phone, Email email, Address address, Set<Tag> tags, DonatedAmount donatedAmount) {
        super(name, phone, email, address, tags);
        requireAllNonNull(donatedAmount);
        this.donatedAmount = donatedAmount;
    }

    public DonatedAmount getDonatedAmount() {
        return donatedAmount;
    }

    /**
     * Compares this Donor to another for equality.
     *
     * @param other The object to compare with.
     * @return True if both Donors have identical attributes.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Donor)) {
            return false;
        }

        Donor otherDonor = (Donor) other;
        return super.equals(otherDonor) && (donatedAmount.equals(otherDonor.donatedAmount));
    }

    /**
     * Returns a hash code for this Donor based on its attributes.
     *
     * @return Hash code of the donor.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, address, tags, donatedAmount);
    }

    /**
     * Returns a string representation of this Donor.
     *
     * @return String representation of the donor.
     */
    @Override
    public String toString() {
        String parentToString = super.toString();

        // Remove the last '}' from parentToString before appending child class fields
        parentToString = parentToString.substring(0, parentToString.length() - 1);

        return parentToString + ", donatedAmount=" + donatedAmount + "}";
    }

    @Override
    public Role getRole() {
        return Role.DONOR;
    }

    /**
     * Compares this Donor to another Donor based on donated amount
     *
     * @param donor the object to be compared.
     */
    @Override
    public int compareTo(Donor donor) {
        return this.donatedAmount.compareTo(donor.donatedAmount);
    }
}
