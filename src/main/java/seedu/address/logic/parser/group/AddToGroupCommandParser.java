package seedu.address.logic.parser.group;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMBERS;
import static seedu.address.logic.parser.ParserUtil.parseMembers;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.group.AddToGroupCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parser for commands of type AddToGroupCommand. Parses the argument by trimming whitespaces and returning a new
 * {@code AddToGroupCommand} with the trimmed argument.
 */
public class AddToGroupCommandParser implements Parser<AddToGroupCommand> {
    public static final String MEMBER_MESSAGE_CONSTRAINTS = "The members should be positive integer indices from "
            + "the latest shown list.";

    public static final String MEMBER_MESSAGE_DUPLICATES = "There should not be duplicated indices:\n";

    /**
     * Given the list of new members to be added to the group, find if there are any duplicate indices.
     *
     * @param members The list of member indices.
     * @return A set containing the duplicated members' indices.
     */
    private static Set<Index> findDuplicates(List<Index> members) {
        return members.stream()
                .filter(index -> Collections.frequency(members, index) > 1)
                .collect(Collectors.toSet());
    }

    /**
     * Converts the set of duplicates to a string that can be printed to the user.
     *
     * @param duplicates The set of duplicated indices.
     * @return the string that represents the set of duplicates.
     */
    private static String duplicatesSetToString(Set<Index> duplicates) {
        return duplicates.stream()
                .map(Index::getOneBased)
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
    }

    @Override
    public AddToGroupCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_GROUP_NAME, PREFIX_MEMBERS);

        if (argMultimap.getValue(PREFIX_GROUP_NAME).isEmpty() || argMultimap.getValue(PREFIX_MEMBERS).isEmpty()
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddToGroupCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_GROUP_NAME, PREFIX_MEMBERS);
        String groupName = argMultimap.getValue(PREFIX_GROUP_NAME).orElse("");
        String membersString = argMultimap.getValue(PREFIX_MEMBERS).orElse("");
        List<Index> members;
        try {
            members = parseMembers(membersString);
        } catch (NumberFormatException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddToGroupCommand.MESSAGE_USAGE),
                    e);
        }
        Set<Index> duplicates = findDuplicates(members);
        if (!duplicates.isEmpty()) {
            String duplicatesAsString = duplicatesSetToString(duplicates);
            throw new ParseException(MEMBER_MESSAGE_DUPLICATES + duplicatesAsString);
        }
        return new AddToGroupCommand(groupName, members);
    }
}
