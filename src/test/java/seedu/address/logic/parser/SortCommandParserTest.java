package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.SortOption.MESSAGE_EMPTY_SORT_OPTION;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.SortCommand;

public class SortCommandParserTest {

    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_noArgs_returnsSortCommand() {
        assertParseSuccess(parser, "", new SortCommand());
    }

    @Test
    public void parse_validSortOption_returnsSortCommand() {
        SortOption sortOption = SortOption.NAME;
        assertParseSuccess(parser, " s/name", new SortCommand(sortOption));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " invalid", expectedMessage);
    }

    @Test
    public void parse_multipleValidSortOptions_throwsParseException() {
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SORT);
        assertParseFailure(parser, " s/name s/age", expectedMessage);
    }

    @Test
    public void parse_emptySortOption_throwsParseException() {
        assertParseFailure(parser, " s/", MESSAGE_EMPTY_SORT_OPTION);
    }

    @Test
    public void parse_invalidSortOption_throwsParseException() {
        assertParseFailure(parser, " s/Invalid", SortOption.MESSAGE_CONSTRAINTS);
    }
}
