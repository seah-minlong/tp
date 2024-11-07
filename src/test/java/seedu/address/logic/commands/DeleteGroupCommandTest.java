package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.testdata.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.group.DeleteGroupCommand;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Group;
import seedu.address.model.person.exceptions.GroupNotFoundException;

/**
 * Contains unit tests for {@code DeleteGroupCommand}.
 */
public class DeleteGroupCommandTest {
    private static Model model;
    private static Model expectedModel;

    @BeforeAll
    public static void setUp() {
        AddressBook addressBook = getTypicalAddressBook();
        addressBook.addGroup(new Group("test"));
        model = new ModelManager(addressBook, new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_groupNotExist_throwsCommandException() {
        String invalidGroupName = "invalidGroupName";
        DeleteGroupCommand command = new DeleteGroupCommand(invalidGroupName);

        assertCommandFailure(command, model, new GroupNotFoundException().getMessage());
    }

    @Test
    public void execute_groupExist_success() {
        String validGroupName = "test";
        DeleteGroupCommand command = new DeleteGroupCommand(validGroupName);
        CommandResult expectedCommandResult =
                new CommandResult(String.format(DeleteGroupCommand.MESSAGE_DELETE_GROUP_SUCCESS, validGroupName), true);
        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
    }
}
