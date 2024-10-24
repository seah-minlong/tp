package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.SortOption;
import seedu.address.model.Model;
import seedu.address.model.person.Volunteer;

/**
 * Sorts the person list based on the specified option.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts the person list.\n"
            + "If no sort option is provided, the list is restored to its original order (i.e., order of addition).\n"
            + "Parameters: " + "[" + PREFIX_SORT + "SORT_OPTION]\n"
            + "Example: " + COMMAND_WORD + " s/name";

    public static final String MESSAGE_DEFAULT_SUCCESS = "Sorted by default order";
    public static final String MESSAGE_SORT_SUCCESS = "Sorted by %s";
    public static final String MESSAGE_UNSUPPORTED_SORT_OPTION = "Unsupported sort option: %s";

    public static final String MESSAGE_SORT_BY_ROLE_CRITERIA_WITH_OTHERS =
            "%1$ss sorted by %2$s. Non-%3$ss are listed after %3$ss.";
    public static final String MESSAGE_SORT_BY_ROLE_CRITERIA_NONE_FOUND =
            "No %ss found. The list remains unsorted.";

    private static final Logger logger = LogsCenter.getLogger(SortCommand.class);

    public final SortOption sortOption;

    /**
     * Constructs a SortCommand with no sort option, which will reset the list to the default order.
     */
    public SortCommand() {
        this.sortOption = null;
    }

    /**
     * Constructs a SortCommand with the specified sort option.
     *
     * @param sortOption The sort option to apply to the list.
     */
    public SortCommand(SortOption sortOption) {
        this.sortOption = sortOption;
    }

    /**
     * Executes the sort command by applying the specified sort option to the person list.
     *
     * @param model The model on which to execute the sorting operation.
     * @return A CommandResult indicating the success or failure of the sort operation.
     * @throws CommandException If an unsupported sort option is provided.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.clearPersonSort();

        if (sortOption == null) {
            return executeDefaultSort(model);
        }

        switch (sortOption) {
        case NAME:
            return executeSortByName(model);
        case HOURS:
            return executeSortByHours(model);
        default:
            // Log that an unexpected sort option has been encountered
            logger.severe("Unexpected sort option encountered: " + sortOption
                    + ". This should not have happened as SortOption should validate inputs.");
            throw new CommandException(String.format(MESSAGE_UNSUPPORTED_SORT_OPTION, sortOption));
        }
    }

    /**
     * Creates a {@code CommandResult} indicating that the list has been sorted to its default order (insertion order).
     *
     * @return A {@code CommandResult}
     */
    private static CommandResult executeDefaultSort(Model model) {
        return new CommandResult(MESSAGE_DEFAULT_SUCCESS);
    }

    /**
     * Sorts the person list by name in alphabetical order.
     *
     * @param model The model on which to apply the name sorting.
     * @return A CommandResult indicating that the list has been sorted by name.
     */
    private CommandResult executeSortByName(Model model) {
        updateSort(model);
        return new CommandResult(String.format(MESSAGE_SORT_SUCCESS, sortOption));
    }

    /**
     * Sorts the person list by hours contributed for volunteers.
     * Volunteers are listed first, sorted by hours, and non-volunteers are listed after volunteers.
     * If there are no volunteers, the list remains unsorted.
     *
     * @param model The model on which to apply the sorting by hours.
     * @return A CommandResult indicating the outcome of the sorting operation:
     *         - If there are only volunteers, the list is sorted by hours.
     *         - If there are both volunteers and non-volunteers, volunteers are sorted by hours and non-volunteers
     *           are listed at the back.
     *         - If there are no volunteers, the list remains unsorted.
     */
    private CommandResult executeSortByHours(Model model) {
        String entityType = "Volunteer";
        String sortCriterion = "hours contributed";

        if (!model.hasPersonsOfType(Volunteer.class)) {
            return new CommandResult(String.format(MESSAGE_SORT_BY_ROLE_CRITERIA_NONE_FOUND,
                    entityType.toLowerCase()));
        }

        updateSort(model);

        if (model.hasOnlyPersonsOfType(Volunteer.class)) {
            return new CommandResult(String.format(MESSAGE_SORT_SUCCESS, sortOption));
        } else {
            return new CommandResult(String.format(MESSAGE_SORT_BY_ROLE_CRITERIA_WITH_OTHERS,
                    entityType, sortCriterion, entityType.toLowerCase()));
        }
    }

    /**
     * Updates the sorting of the person list in the given {@code Model} based on the current {@code SortOption}.
     *
     * This method retrieves the appropriate comparator from the {@code SortOption} and applies it to
     * the person list in the {@code Model} to update the sorting order.
     *
     * @param model The model containing the person list to be sorted. Must not be null.
     */
    private void updateSort(Model model) {
        assert sortOption != null;
        model.updatePersonListSort(sortOption.getComparator());
    }

    @Override
    public boolean equals(Object other) {
        // If both commands have the same sortOption, they are considered equal
        if (other == this) {
            return true;
        }

        if (!(other instanceof SortCommand)) {
            return false;
        }

        SortCommand otherCommand = (SortCommand) other;
        return (sortOption == null && otherCommand.sortOption == null)
                || (sortOption != null && sortOption.equals(otherCommand.sortOption));
    }
}
