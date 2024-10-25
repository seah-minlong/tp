package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Group;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.GroupNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final SortedList<Person> sortedPersons;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        sortedPersons = new SortedList<>(filteredPersons);
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
        logger.info("Address book data has been reset.");
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    @Override
    public void addGroup(Group group) {
        requireNonNull(group);
        addressBook.addGroup(group);
    }

    @Override
    public void removeGroup(String groupName) {
        requireNonNull(groupName);
        Group group = new Group(groupName);
        addressBook.removeGroup(group);
    }

    @Override
    public void setGroup(Group oldGroup, Group newGroup) {
        requireNonNull(oldGroup);
        requireNonNull(newGroup);
        addressBook.setGroup(oldGroup, newGroup);
    }

    @Override
    public Group getGroup(String groupName) {
        requireNonNull(groupName);
        return addressBook.getGroup(groupName);
    }

    public String getGroupNames() {
        return addressBook.getGroupNames();
    }

    //=========== Person List Accessors ======================================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     *
     * The returned list is both sorted and filtered based on the current comparator and filter predicate
     * applied.
     *
     * @return a filtered and sorted list of persons.
     */
    @Override
    public ObservableList<Person> getPersonList() {
        // sortedPersons observes changes in filteredPersons
        return sortedPersons;
    }

    @Override
    public boolean hasPersonsOfType(Class<? extends Person> personType) {
        requireNonNull(personType);
        return filteredPersons.stream().anyMatch(personType::isInstance);
    }

    @Override
    public boolean hasOnlyPersonsOfType(Class<? extends Person> personType) {
        requireNonNull(personType);
        return filteredPersons.stream().allMatch(personType::isInstance);
    }

    //=========== Filtered Person List Accessors =============================================================

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== Group Accessors ===========================================================================

    @Override
    public void filterByGroup(String groupName) throws GroupNotFoundException {
        Group group = addressBook.getGroup(groupName);
        updateFilteredPersonList(group::containsExact);
    }

    //=========== Sorted Person List Accessors =============================================================
    @Override
    public void updatePersonListSort(Comparator<Person> comparator) {
        requireNonNull(comparator);
        sortedPersons.setComparator(comparator);
        logger.fine("Person list sorted with new comparator.");
    }

    @Override
    public void clearPersonSort() {
        sortedPersons.setComparator(null);
        logger.fine("Cleared person list sort. Reset to default order.");
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && sortedPersons.equals(otherModelManager.sortedPersons);
    }

}
