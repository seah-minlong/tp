package seedu.address.logic.commands.group;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMBERS;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Group;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicateGroupException;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.GroupNotFoundException;

/**
 * Command to add new members to a group.
 */
public class AddToGroupCommand extends Command {
    public static final String COMMAND_WORD = "addToGroup";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds contacts to a existing group of specified name, with "
            + "the specified persons (referenced by index of current list) inside.\n"
            + "Parameters: "
            + PREFIX_GROUP_NAME + "GROUP_NAME "
            + PREFIX_MEMBERS + " INDEX [MORE_INDICES...]\n"
            + "Example: " + COMMAND_WORD + " g/blood drive m/ 1 4 6";

    public static final String MESSAGE_ADD_TO_GROUP_SUCCESS = "The following users were added to the group %s:\n";
    public static final String MESSAGE_ADD_TO_GROUP_DUPLICATES = "The following users were already in the group %s "
            + "and will remain in the group, the rest of the users have been added accordingly: \n";
    public static final String MESSAGE_GROUP_NOT_EXISTS = "There is no group with name %s.";

    private final String groupName;

    private final List<Index> members;

    private List<Person> duplicates = new ArrayList<>();
    private List<Person> newMembers = new ArrayList<>();

    /**
     * Creates a new AddToGroupCommand.
     * @param groupName The group name
     * @param members The persons to be included (referenced by index)
     */
    public AddToGroupCommand(String groupName, List<Index> members) {
        this.groupName = groupName;
        this.members = members;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getPersonList();
        Group existingGroup;
        try {
            existingGroup = model.getGroup(groupName);
        } catch (GroupNotFoundException gnfe) {
            throw new CommandException(String.format(MESSAGE_GROUP_NOT_EXISTS, groupName));
        }
        Group newGroup = this.createGroupWithNewMembers(existingGroup, members, lastShownList);
        try {
            model.setGroup(existingGroup, newGroup);
        } catch (DuplicateGroupException e) {
            throw new CommandException(e.getMessage());
        }
        return new CommandResult(formatSuccessCommandResultMessage());
    }

    /**
     * Formats the message to output to the user after the execution of the command.
     * This is non-trivial as new entries and duplicates are taken into account to
     * output the correct string format.
     *
     * @return a string which should go into the CommandResult.
     */
    private String formatSuccessCommandResultMessage() {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format(MESSAGE_ADD_TO_GROUP_SUCCESS, groupName));
        this.newMembers.forEach(person -> builder.append(person.getName()).append('\n'));
        if (!this.duplicates.isEmpty()) {
            builder.append(String.format(MESSAGE_ADD_TO_GROUP_DUPLICATES, groupName));
            this.duplicates.forEach(person -> builder.append(person.getName()).append('\n'));
        }
        return builder.toString();
    }

    /**
     * To enforce immutability, this function creates a new group with the added members.
     *
     * @param existingGroup The group object of the group before addition of members.
     * @param members The indices of the members to be added, based on the lastShownList.
     * @param lastShownList The list of Person's objects last displayed to the user.
     * @return a new Group object with the added person objects.
     */
    private Group createGroupWithNewMembers(Group existingGroup,
                                                     List<Index> members, List<Person> lastShownList) {
        Group newGroup = new Group(this.groupName);
        newGroup.setPersons(existingGroup.asUnmodifiableObservableList());
        members.stream()
                .map(member -> lastShownList.get(member.getZeroBased()))
                .forEach(person -> {
                    try {
                        newGroup.add(person);
                        newMembers.add(person);
                    } catch (DuplicatePersonException dpe) {
                        this.duplicates.add(person);
                    }
                });
        return newGroup;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof AddToGroupCommand e)) {
            return false;
        }
        return groupName.equals(e.groupName) && members.equals(e.members);
    }
}
