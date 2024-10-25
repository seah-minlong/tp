package seedu.address.logic.parser.group;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP_NAME;

import java.util.List;

import seedu.address.logic.commands.group.EditGroupNameCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parser for commands of type EditGroupNameCommand. Parses the argument by trimming whitespaces and returning a new
 * {@code EditGroupNameCommand} with the trimmed argument.
 */
public class EditGroupNameCommandParser implements Parser<EditGroupNameCommand> {
    @Override
    public EditGroupNameCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_GROUP_NAME);
        if (argMultimap.getAllValues(PREFIX_GROUP_NAME).isEmpty() || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditGroupNameCommand.MESSAGE_USAGE));
        }
        List<String> names = argMultimap.getAllValues(PREFIX_GROUP_NAME);
        if (names.size() != 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditGroupNameCommand.MESSAGE_USAGE));
        }
        return new EditGroupNameCommand(names.get(0), names.get(1));
    }
}
