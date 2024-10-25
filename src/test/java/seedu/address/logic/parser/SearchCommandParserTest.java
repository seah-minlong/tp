package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

public class SearchCommandParserTest {

    private SearchCommandParser parser = new SearchCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validNameArgs_returnsSearchCommand() {
        // no leading and trailing whitespaces
        SearchCommand expectedSearchCommand =
                new SearchCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")), null);
        assertParseSuccess(parser, " n/Alice Bob", expectedSearchCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " n/ \n Alice \n \t Bob  \t", expectedSearchCommand);
    }

    @Test
    public void parse_validTagArgs_returnsSearchCommand() {
        // no leading and trailing whitespaces for tags
        SearchCommand expectedSearchCommand =
                new SearchCommand(
                        new TagContainsKeywordsPredicate(Arrays.asList("friends", "colleague")), null);
        assertParseSuccess(parser, " t/friends colleague", expectedSearchCommand);

        // multiple whitespaces between keywords for tags
        assertParseSuccess(parser, " t/ \n friends \n \t colleague  \t", expectedSearchCommand);
    }

    @Test
    public void parse_emptyNameArg_throwsParseException() {
        assertParseFailure(parser, " n/ t/ friends",
                "The prefix cannot be empty. Please input a prefix.");
    }

    @Test
    public void parse_emptyTagArg_throwsParseException() {
        assertParseFailure(parser, " n/Alice t/", "The prefix cannot be empty. Please input a prefix.");
    }

    @Test
    public void parse_multipleNames_throwsParseException() {
        assertParseFailure(parser, " n/Alice n/Bob",
                "Multiple values specified for the following single-valued field(s): n/");
    }

    @Test
    public void parse_multipleTags_throwsParseException() {
        assertParseFailure(parser, " t/friend t/colleague",
                "Multiple values specified for the following single-valued field(s): t/");
    }

}
