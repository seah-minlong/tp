package seedu.address.model.person.comparators;

import java.util.Comparator;

import seedu.address.model.person.Person;

/**
 * A generic comparator that prioritizes a specific role type.
 * Instances of the role type are placed before non-role types, and are sorted by the specified criterion.
 * Non-role types retain their original order.
 *
 * @param <T> The type of person that should be prioritized (e.g., Donor, Volunteer).
 */
public class RoleComparator<T extends Person & Comparable<T>> implements Comparator<Person> {

    private final Class<T> roleClass;

    /**
     * Constructs a {@code RoleComparator}.
     *
     * @param roleClass The class of the role type to prioritize (e.g., Donor.class, Volunteer.class).
     */
    public RoleComparator(Class<T> roleClass) {
        this.roleClass = roleClass;
    }

    @Override
    public int compare(Person p1, Person p2) {
        boolean isP1Role = roleClass.isInstance(p1);
        boolean isP2Role = roleClass.isInstance(p2);

        if (isP1Role && isP2Role) {
            T r1 = roleClass.cast(p1);
            T r2 = roleClass.cast(p2);
            return r1.compareTo(r2); // Sort based on the role comparison logic (e.g., donation amount, hours)
        } else if (isP1Role) {
            // p1 is the specified role, p2 is not; prioritize p1
            return -1;
        } else if (isP2Role) {
            // p2 is the specified role, p1 is not; prioritize p2
            return 1;
        } else {
            // Neither are of the specified role; maintain original order
            return 0; // Maintain the original insertion order for non-role persons
        }
    }
}
