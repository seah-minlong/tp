package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.SortOption;
import seedu.address.model.Model;

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

    public static final String MESSAGE_SORT_BY_ROLE_CRITERIA_NONE_FOUND =
            "No %ss found. The list is reset to its default order.";

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
            return new CommandResult(MESSAGE_DEFAULT_SUCCESS);
        }

        if (!model.hasPersonsOfType(sortOption.getRelatedClass())) {
            return new CommandResult(String.format(MESSAGE_SORT_BY_ROLE_CRITERIA_NONE_FOUND,
                    sortOption.getRoleAsString()));
        }

        updateSort(model);

        return new CommandResult(String.format(MESSAGE_SORT_SUCCESS, sortOption));
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
