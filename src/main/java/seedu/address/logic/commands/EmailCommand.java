package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Command that copies the emails of the persons in the current list to clipboard, with comma separations.
 */
public class EmailCommand extends Command {
    public static final String COMMAND_WORD = "email";

    public static final String MESSAGE_SUCCESS = "Copied emails to clipboard";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        String emails = collateEmails(model);
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(emails);
        clipboard.setContent(url);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Collates emails from all persons in the current list into a {@code String}, separated by commas.
     * @return The string representing the collated emails.
     */
    private String collateEmails(Model model) {
        StringBuilder emails = new StringBuilder();
        for (Person p : model.getPersonList()) {
            emails.append(p.getEmail().toString());
            emails.append(',');
        }
        emails.deleteCharAt(emails.length() - 1);
        return emails.toString();
    }
}
