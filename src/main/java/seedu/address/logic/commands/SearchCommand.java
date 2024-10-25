package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Group;
import seedu.address.model.person.GroupContainsKeywordsPredicate;
import seedu.address.model.person.GroupList;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.GroupNotFoundException;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class SearchCommand extends Command {

    public static final String COMMAND_WORD = "search";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Searches all persons whose names or tags contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "At least one of the following parameters must be provided.\n"
            + "Parameters: " + "PREFIX/ KEYWORD [MORE_PREFIX/ KEYWORD...] \n"
            + "You can search by name, tag, or both."
            + " If both are provided, only persons matching both criteria will be shown.\n"
            + "Example 1: " + COMMAND_WORD + " n/ alice\n"
            + "Example 2: " + COMMAND_WORD + " t/ friend\n"
            + "Example 3: " + COMMAND_WORD + " n/ alice t/ friend\n"
            + "Example 4: " + COMMAND_WORD + " t/ friend n/ alice";

    public static final String MESSAGE_NO_FOUND_GROUP = "There is no such group!";

    private final Predicate<Person> predicate;
    private final String groupName;

    /**
     * Constructs a {@code SearchCommand} to filter the person list using the specified predicate and group name.
     *
     * @param predicate The predicate that filters the list of persons based on name and/or tag criteria.
     * @param groupName The name of the group to further filter the persons.
     *                  If empty or {@code null}, no group filtering is applied.
     */
    public SearchCommand(Predicate<Person> predicate, String groupName) {
        this.predicate = predicate;
        this.groupName = groupName;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Predicate<Person> finalPredicate = predicate;

        // If a group name is provided, perform a group lookup and 1bine it with the existing predicate
        if (groupName != null && !groupName.isEmpty()) {
            // Fetch the GroupList
            GroupList groupList = model.getAddressBook().getGroupList();
            try {
                // Retrieve the group by name
                Group group = groupList.get(groupName);
                GroupContainsKeywordsPredicate groupPredicate = new GroupContainsKeywordsPredicate(group);

                // Combine predicates
                finalPredicate = finalPredicate == null ? groupPredicate : finalPredicate.and(groupPredicate);
            } catch (GroupNotFoundException e) {
                throw new CommandException(MESSAGE_NO_FOUND_GROUP);
            }
        }
        model.updateFilteredPersonList(finalPredicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SearchCommand)) {
            return false;
        }

        SearchCommand otherSearchCommand = (SearchCommand) other;
        return predicate.equals(otherSearchCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
