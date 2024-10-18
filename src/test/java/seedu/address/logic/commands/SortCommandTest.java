package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.SortCommand.MESSAGE_SORT_SUCCESS;
import static seedu.address.model.Model.COMPARATOR_SORT_BY_NAME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.logic.parser.SortOption;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for SortCommand.
 */
public class SortCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validSortOption_success() {
        SortOption sortOption = new SortOption(SortOption.SORT_NAME);
        SortCommand sortCommand = new SortCommand(sortOption);

        // Prepare the expected sorted list
        List<Person> expectedSortedList = new ArrayList<>(model.getAddressBook().getPersonList());
        expectedSortedList.sort(COMPARATOR_SORT_BY_NAME);

        // Update expectedModel to match the sorted state
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.updatePersonListSort(COMPARATOR_SORT_BY_NAME);

        assertCommandSuccess(sortCommand, model,
                String.format(MESSAGE_SORT_SUCCESS, sortOption), expectedModel);

        // Check if list is sorted correctly
        ObservableList<Person> actualList = model.getPersonList();
        assertEquals(expectedSortedList, new ArrayList<>(actualList));
    }

    @Test
    public void execute_nullSortOption_resetsToDefaultOrder() {
        SortCommand sortCommand = new SortCommand(null); // Passing null instead of SortOption

        // Prepare the expected default list (insertion order)
        List<Person> expectedDefaultList = new ArrayList<>(model.getAddressBook().getPersonList());

        // Update expectedModel to match the default state
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.clearPersonSort(); // Reset sorting in the expected model

        assertCommandSuccess(sortCommand, model, SortCommand.MESSAGE_DEFAULT_SUCCESS, expectedModel);

        // Check if list is reset to default order (insertion order)
        ObservableList<Person> actualList = model.getPersonList();
        assertEquals(expectedDefaultList, new ArrayList<>(actualList));
    }

    @Test
    public void equals() {
        SortOption sortOptionName = new SortOption(SortOption.SORT_NAME);
        SortCommand sortByNameCommand1 = new SortCommand(sortOptionName);
        SortCommand sortByNameCommand2 = new SortCommand(sortOptionName);

        // Same object -> returns true
        assertEquals(sortByNameCommand1, sortByNameCommand1);

        // Same sort option -> returns true
        assertEquals(sortByNameCommand1, sortByNameCommand2);

        // Null -> returns false
        assertNotEquals(sortByNameCommand1, null);

        // Different types -> returns false
        assertNotEquals(sortByNameCommand1, new ListCommand());
    }
}