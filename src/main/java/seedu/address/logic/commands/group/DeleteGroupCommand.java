package seedu.address.logic.commands.group;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP_NAME;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.exceptions.GroupNotFoundException;

/**
 * Command to delete a new group with the specified name and persons inside.
 */
public class DeleteGroupCommand extends Command {
    public static final String COMMAND_WORD = "deleteGroup";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes the group with the specified name.\n"
            + "Parameters: "
            + PREFIX_GROUP_NAME + "GROUP_NAME\n"
            + "Example: " + COMMAND_WORD + "blood drive";

    public static final String MESSAGE_DELETE_GROUP_SUCCESS = "Deleted group %s\n";

    private final String groupName;

    /**
     * Creates a new DeleteGroupCommand.
     * @param groupName The group name
     */
    public DeleteGroupCommand(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        try {
            model.removeGroup(groupName);
        } catch (GroupNotFoundException e) {
            throw new CommandException(e.getMessage());
        }
        return new CommandResult(String.format(MESSAGE_DELETE_GROUP_SUCCESS, groupName), true);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof DeleteGroupCommand e)) {
            return false;
        }
        return groupName.equals(e.groupName);
    }
}
