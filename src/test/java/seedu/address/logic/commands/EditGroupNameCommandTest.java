package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.testdata.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.group.EditGroupNameCommand;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.GroupBuilder;

public class EditGroupNameCommandTest {

    private static Model model;

    @BeforeAll
    public static void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addGroup(new GroupBuilder<>().withName("NUS").build());
    }

    @Test
    public void execute_success() {
        EditGroupNameCommand editGroupNameCommand = new EditGroupNameCommand("NUS", "SMU");
        String expectedMessage = String.format(EditGroupNameCommand.MESSAGE_EDIT_GROUP_SUCCESS, "NUS", "SMU");
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, true);

        Model expectedModel = new ModelManager(new AddressBook(getTypicalAddressBook()), new UserPrefs());
        expectedModel.addGroup(new GroupBuilder<>().withName("SMU").build());

        System.out.println("Expected: " + expectedModel.getGroupNames());
        System.out.println("Actual: " + model.getGroupNames());
        assertCommandSuccess(editGroupNameCommand, model, expectedCommandResult, expectedModel);
    }
}
