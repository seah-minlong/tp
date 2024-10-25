package seedu.address.logic.commands.group;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;

/**
 * Lists the names of every existing {@code Group}.
 */
public class ListGroupsCommand extends Command {
    public static final String COMMAND_WORD = "listGroups";

    @Override
    public CommandResult execute(Model model) {
        String groupNames = model.getGroupNames();
        return new CommandResult(groupNames);
    }
}
