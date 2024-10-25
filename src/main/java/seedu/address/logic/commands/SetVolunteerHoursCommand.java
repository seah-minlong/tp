package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FOR_PERSON_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HOURS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Hours;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Volunteer;
import seedu.address.model.tag.Tag;

/**
 * Sets the hours of an existing volunteer in the address book
 */
public class SetVolunteerHoursCommand extends Command {
    public static final String COMMAND_WORD = "setHours";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the hours of the volunteer identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_HOURS + "HOURS\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_HOURS + "20";

    public static final String MESSAGE_SET_VOLUNTEER_HOURS_SUCCESS = "New hours for: %1$s";
    public static final String MESSAGE_NOT_EDITED = "The new number of hours must be provided.";
    private final Index index;
    private final String newHours;

    /**
     * @param index of the volunteer in the filtered person list to change hours for
     * @param newHours the new hours to input for the person
     */
    public SetVolunteerHoursCommand(Index index, String newHours) {
        requireNonNull(index);

        this.index = index;
        this.newHours = newHours;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        if (!(personToEdit instanceof Volunteer)) {
            throw new CommandException(MESSAGE_INVALID_COMMAND_FOR_PERSON_TYPE);
        }
        Volunteer editedVolunteer = createUpdatedHoursVolunteer(personToEdit, new Hours(newHours));
        model.setPerson(personToEdit, editedVolunteer);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(
                String.format(MESSAGE_SET_VOLUNTEER_HOURS_SUCCESS,
                        Messages.formatSetVolunteerHours(editedVolunteer)));
    }

    private static Volunteer createUpdatedHoursVolunteer(Person volunteerToEdit, Hours newHours) {
        Name name = volunteerToEdit.getName();
        Phone phone = volunteerToEdit.getPhone();
        Email email = volunteerToEdit.getEmail();
        Address address = volunteerToEdit.getAddress();
        Set<Tag> tags = volunteerToEdit.getTags();
        return new Volunteer(name, phone, email, address, tags, newHours);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SetVolunteerHoursCommand)) {
            return false;
        }

        SetVolunteerHoursCommand otherSetVolunteerHoursCommand =
                (SetVolunteerHoursCommand) other;
        return index.equals(otherSetVolunteerHoursCommand.index)
                && newHours.equals(otherSetVolunteerHoursCommand.newHours);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("newHours", newHours)
                .toString();
    }
}
