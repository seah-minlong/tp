package seedu.address.model;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Group;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicateGroupException;
import seedu.address.model.person.exceptions.GroupNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the person list */
    ObservableList<Person> getPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Adds {@code group} to the groups in the {@code AddressBook}.
     */
    void addGroup(Group group) throws DuplicateGroupException;

    /**
     * Removes group with name {@code groupName} from the groups in the {@code AddressBook}.
     */
    void removeGroup(String groupName) throws GroupNotFoundException;

    /**
     * Edits the group's name from the old name to the new name.
     */
    void setGroup(Group oldGroup, Group newGroup) throws GroupNotFoundException, DuplicateGroupException;

    /**
     * Gets the group with name {@code groupName} from the groups in the {@code AddressBook}
     */
    Group getGroup(String groupName) throws GroupNotFoundException;

    /**
     * Returns the names of every {@code Group} in the {@code AddressBook}.
     */
    String getGroupNames();

    /**
     * Updates the sort order of the person list to the given {@code comparator}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updatePersonListSort(Comparator<Person> comparator);

    /**
     * Clears any sorting applied to the person list.
     */
    void clearPersonSort();

    /**
     * Filters for persons in the group with name {@code groupName}.
     */
    void filterByGroup(String groupName);

    /**
     * Returns true if there is at least one person in the list of the specified {@code personType}.
     *
     * @param personType The class type of the person (e.g., Volunteer.class, Donor.class).
     * @return True if at least one person in the list matches the specified type, otherwise false.
     * @throws NullPointerException if {@code personType} is null.
     */
    boolean hasPersonsOfType(Class<? extends Person> personType);

    /**
     * Returns true if all persons in the list are of the specified {@code personType}.
     *
     * @param personType The class type of the person (e.g., Volunteer.class, Donor.class).
     * @return True if all persons in the list match the specified type, otherwise false.
     * @throws NullPointerException if {@code personType} is null.
     */
    boolean hasOnlyPersonsOfType(Class<? extends Person> personType);

}
