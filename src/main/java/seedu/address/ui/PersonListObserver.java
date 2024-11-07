package seedu.address.ui;

/**
 * An interface for classes that want to be notified of changes to the person list.
 * Classes implementing this interface can register with a {@code PersonListPanel}
 * to receive updates whenever there is a change in the person list, such as
 * when a person is added or removed.
 */
public interface PersonListObserver {
    /**
     * An interface for classes that want to be notified of changes to the person list.
     * Classes implementing this interface can register with a {@code PersonListPanel}
     * to receive updates whenever there is a change in the person list, such as
     * when a person is added or removed.
     */
    void onPersonListChanged();
}
