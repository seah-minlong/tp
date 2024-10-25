package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT;
import static seedu.address.logic.parser.SortOption.MESSAGE_EMPTY_SORT_OPTION;

import java.util.Optional;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortCommand object.
 */
public class SortCommandParser implements Parser<SortCommand> {

    @Override
    public SortCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SORT);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        // Ensure there are no duplicate sort prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_SORT);

        // Checks if sortOption prefix was used
        Optional<String> sortOption = argMultimap.getValue(PREFIX_SORT);

        if (sortOption.isEmpty()) {
            // No sort option
            return new SortCommand();
        }

        String sortOptionValue = sortOption.get().trim();
        assert sortOptionValue != null : "Sort option value should not be null here";

        if (sortOptionValue.isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_SORT_OPTION);
        }

        // Ensure sort option is supported
        SortOption validSortOption = ParserUtil.parseSortOption(sortOptionValue);
        assert validSortOption != null : "Parsed SortOption should not be null";

        return new SortCommand(validSortOption);
    }
}
