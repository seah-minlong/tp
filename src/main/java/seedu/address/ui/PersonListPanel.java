package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final List<PersonListObserver> observers = new ArrayList<>();
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList) {
        super(FXML);
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());

        // Add a listener to detect changes in the person list
        personList.addListener((ListChangeListener<Person>) change -> {
            while (change.next()) {
                // Notify the observers if there are any additions or removals
                if (change.wasAdded() || change.wasRemoved()) {
                    notifyObservers();
                }
            }
        });
    }

    /**
     * Registers an observer to notify about changes in the person list.
     */
    public void addObserver(PersonListObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies all registered observers of changes in the person list.
     */
    private void notifyObservers() {
        for (PersonListObserver observer : observers) {
            observer.onPersonListChanged();
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1).getRoot());
            }
        }
    }

}
