package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * A group of persons which has an identifying name, and can be made and edited by the user.
 * Uses the implementation of UniquePersonList
 *
 * @see UniquePersonList
 */
public class Group extends UniquePersonList {
    private final String name;

    /**
     * Creates a group named {@code name}.
     */
    public Group(String name) {
        super();
        this.name = name;
    }

    /**
     * @return Name of the group.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if {@code group} has the same name as this group.
     * @return True if the groups have the same name, false otherwise.
     */
    public boolean isSameName(Group group) {
        requireNonNull(group);
        return name.equals(group.name);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Group otherGroup)) {
            return false;
        }
        return super.equals(o) && name.equals(otherGroup.name);
    }
}
