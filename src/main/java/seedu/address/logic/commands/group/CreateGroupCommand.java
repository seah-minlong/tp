package seedu.address.logic.commands.group;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMBERS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Group;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicateGroupException;

/**
 * Command to create a new group with the specified name and persons inside.
 */
public class CreateGroupCommand extends Command {
    public static final String COMMAND_WORD = "createGroup";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates a new group of specified name, with "
            + "the specified persons (referenced by index of current list) inside.\n"
            + "Parameters: "
            + PREFIX_GROUP_NAME + "GROUP_NAME "
            + PREFIX_MEMBERS + " INDEX [MORE_INDICES...]\n"
            + "Example: " + COMMAND_WORD + " g/blood drive m/ 1 4 6";

    public static final String MESSAGE_CREATE_GROUP_SUCCESS = "Created group %s\n";

    private final String groupName;

    private final List<Index> members;

    /**
     * Creates a new CreateGroupCommand.
     * @param groupName The group name
     * @param members The persons to be included (referenced by index)
     */
    public CreateGroupCommand(String groupName, List<Index> members) {
        this.groupName = groupName;
        this.members = members;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Group newGroup = new Group(groupName);
        List<Person> lastShownList = model.getPersonList();

        for (Index i : members) {
            if (i.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            Person personToAdd = lastShownList.get(i.getZeroBased());
            newGroup.add(personToAdd);
        }

        try {
            model.addGroup(newGroup);
        } catch (DuplicateGroupException e) {
            throw new CommandException(e.getMessage());
        }
        return new CommandResult(String.format(MESSAGE_CREATE_GROUP_SUCCESS, groupName), true);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof CreateGroupCommand e)) {
            return false;
        }
        return groupName.equals(e.groupName) && members.equals(e.members);
    }
}
