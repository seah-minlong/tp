package seedu.address.logic.commands.group;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP_NAME;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Group;
import seedu.address.model.person.exceptions.DuplicateGroupException;
import seedu.address.model.person.exceptions.GroupNotFoundException;

/**
 * Command to edit a group's name while maintaining the members.
 */
public class EditGroupNameCommand extends Command {
    public static final String COMMAND_WORD = "editGroupName";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the name of the group "
            + "with the specified name to the new name.\n"
            + "Parameters: "
            + PREFIX_GROUP_NAME + "GROUP_NAME "
            + PREFIX_GROUP_NAME + "NEW_GROUP_NAME\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_GROUP_NAME
            + "blood drive 2023 " + PREFIX_GROUP_NAME + "new blood drive 2024";

    public static final String MESSAGE_EDIT_GROUP_SUCCESS = "Edited group name from %s to %s\n";

    public static final String MESSAGE_EDIT_GROUP_NAME_EXISTS = "There already exists a group with "
            + "the new name you have chosen.";
    public static final String MESSAGE_EDIT_GROUP_NOT_FOUND = "The existing group with the given name "
            + "could not be found.";

    private final String oldGroupName;
    private final String newGroupName;

    /**
     * Creates a new EditGroupNameCommand.
     * @param oldGroupName The old groupname.
     * @param newGroupName The new groupname.
     */
    public EditGroupNameCommand(String oldGroupName, String newGroupName) {
        this.oldGroupName = oldGroupName;
        this.newGroupName = newGroupName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Group existingGroup;
        try {
            existingGroup = model.getGroup(oldGroupName);
        } catch (GroupNotFoundException e) {
            throw new CommandException(MESSAGE_EDIT_GROUP_NOT_FOUND);
        }

        Group newGroup = createRenamedGroup(newGroupName, existingGroup);
        try {
            model.setGroup(existingGroup, newGroup);
        } catch (GroupNotFoundException gnfe) {
            throw new CommandException(MESSAGE_EDIT_GROUP_NOT_FOUND);
        } catch (DuplicateGroupException dge) {
            throw new CommandException(MESSAGE_EDIT_GROUP_NAME_EXISTS);
        }
        return new CommandResult(String.format(MESSAGE_EDIT_GROUP_SUCCESS, oldGroupName, newGroupName));
    }

    /**
     * Returns a new group with a new name {@code newName} and the same
     * members as the current group.
     */
    private static Group createRenamedGroup(String newName, Group existingGroup) {
        Group newGroup = new Group(newName);
        newGroup.setPersons(existingGroup.asUnmodifiableObservableList());
        return newGroup;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof EditGroupNameCommand e)) {
            return false;
        }
        return oldGroupName.equals(e.oldGroupName) && newGroupName.equals(e.newGroupName);
    }
}
