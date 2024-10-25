package seedu.address.model.person;

import java.util.function.Predicate;

/**
 * Tests that a {@code Person} belongs to a specific {@code Group}.
 */
public class GroupContainsKeywordsPredicate implements Predicate<Person> {
    private final Group group;

    public GroupContainsKeywordsPredicate(Group group) {
        this.group = group;
    }

    @Override
    public boolean test(Person person) {
        return group.containsExact(person);
    }

    @Override
    public boolean equals(Object other) {
        // Check if the object is the same instance
        if (other == this) {
            return true;
        }

        // Check if the other object is an instance of GroupContainsKeywordsPredicate
        if (!(other instanceof GroupContainsKeywordsPredicate)) {
            return false;
        }

        // Cast the other object and compare the group fields
        GroupContainsKeywordsPredicate otherPredicate = (GroupContainsKeywordsPredicate) other;
        return group.equals(otherPredicate.group);
    }

}
