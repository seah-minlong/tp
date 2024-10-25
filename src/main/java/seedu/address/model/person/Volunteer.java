package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;




/**
 * Represents a Volunteer in the address book.
 * Inherits from Person and includes hours contributed by the volunteer.
 * Immutable class as Volunteer details are subject to changes.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Volunteer extends Person implements Comparable<Volunteer> {

    private final Hours hours;

    /**
     * Constructs a {@code Volunteer}.
     *
     * @param name Name of the volunteer.
     * @param phone Phone number of the volunteer.
     * @param email Email address of the volunteer.
     * @param address Address of the volunteer.
     * @param tags Tags associated with the volunteer.
     * @param hours Hours contributed by the volunteer.
     */
    public Volunteer(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Hours hours) {
        super(name, phone, email, address, tags);
        requireAllNonNull(hours);
        this.hours = hours;
    }

    /**
     * Gets the number of hours that the volunteer has contributed
     *
     * @return
     */
    public Hours getHours() {
        return this.hours;
    }

    /**
     * Compares this Volunteer to another for equality.
     *
     * @param other The object to compare with.
     * @return True if both Volunteers have identical attributes.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Volunteer)) {
            return false;
        }

        Volunteer otherVolunteer = (Volunteer) other;
        return super.equals(other) && hours.equals(otherVolunteer.hours);
    }

    /**
     * Returns a hash code for this Volunteer based on its attributes.
     *
     * @return Hash code of the volunteer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, address, tags, hours);
    }


    /**
     * Compares this volunteer with another volunteer based on hours contributed.
     *
     * @param volunteer the object to be compared.
     */
    @Override
    public int compareTo(Volunteer volunteer) {
        return this.hours.compareTo(volunteer.hours);
    }

    /**
     * Returns a string representation of this Volunteer.
     *
     * @return String representation of the volunteer.
     */
    @Override
    public String toString() {
        String parentToString = super.toString();

        // Remove the last '}' from parentToString before appending child class fields
        parentToString = parentToString.substring(0, parentToString.length() - 1);

        return parentToString + ", hours=" + hours + "}";
    }

    @Override
    public Role getRole() {
        return Role.VOLUNTEER;
    }
}
