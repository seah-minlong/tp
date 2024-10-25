package seedu.address.model.person;


import static seedu.address.logic.parser.CliSyntax.PREFIX_DONATED_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HOURS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARTNERSHIP_END_DATE;

import java.util.Set;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * A factory class to create instances of {@code Person} subclasses based on the specified {@code Role}.
 * <p>
 * This factory uses the provided role and other person attributes to determine which type of
 * {@code Person} subclass to instantiate (e.g., {@code Volunteer}, {@code Donor}, or {@code Partner}).
 * <p>
 * Each role requires specific attributes that are checked during the creation process. If a required
 * attribute is missing or invalid, a {@code ParseException} is thrown. The factory relies on the
 * {@code ArgumentMultimap} to supply role-specific fields.
 */
public class PersonFactory {
    private static final String MISSING_HOURS_MESSAGE = "Missing hours! For a Volunteer, you must specify hours "
            + "contributed by the volunteer using the h/ prefix.";
    private static final String MISSING_DONATED_AMOUNT_MESSAGE = "Missing donated amount! "
            + "For a Donor, you must specify a donated amount using the d/ prefix.";
    private static final String MISSING_END_DATE_MESSAGE = "Missing partnership end date! "
            + "For a Partner, you must specify a partnership end date using the ped/ prefix.";
    private static final String HOURS = "hours";
    private static final String DONATED_AMOUNT = "donatedAmount";
    private static final String END_DATE = "partnershipEndDate";

    private static final String HOURS_EXIST_ERROR = "Hours field should not exist for role: ";
    private static final String AMOUNT_EXIST_ERROR = "Donated Amount field should not exist for role: ";
    private static final String END_DATE_EXIST_ERROR = "Partnership End Date field should not exist for role: ";
    /**
     * Creates a {@code Person} instance based on the specified {@code Role}.
     *
     * @param role        The role of the person to create.
     * @param name        The name of the person.
     * @param phone       The phone number of the person.
     * @param email       The email address of the person.
     * @param address     The address of the person.
     * @param tags        The tags associated with the person.
     * @param argMultimap A map containing all parsed arguments, used to retrieve additional fields for specific roles.
     * @return A new instance of a subclass of {@code Person} based on the role.
     * @throws ParseException If a required field is missing or cannot be parsed.
     */
    public static Person createPerson(Role role, Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                                      ArgumentMultimap argMultimap) throws ParseException {
        switch (role) {
        case VOLUNTEER:
            String hoursValue = checkRequiredField(argMultimap, PREFIX_HOURS, MISSING_HOURS_MESSAGE);
            Hours hours = ParserUtil.parseHours(hoursValue);
            return new Volunteer(name, phone, email, address, tags, hours);

        case DONOR:
            String donatedAmountValue = checkRequiredField(argMultimap, PREFIX_DONATED_AMOUNT,
                    MISSING_DONATED_AMOUNT_MESSAGE);
            DonatedAmount donatedAmount =
                    ParserUtil.parseDonatedAmount(donatedAmountValue);
            return new Donor(name, phone, email, address, tags, donatedAmount);
        case PARTNER:
            String partnershipEndDateValue = checkRequiredField(argMultimap, PREFIX_PARTNERSHIP_END_DATE,
                    MISSING_END_DATE_MESSAGE);
            Date partnershipEndDate =
                    ParserUtil.parsePartnershipEndDate(partnershipEndDateValue);
            return new Partner(name, phone, email, address, tags, partnershipEndDate);
        case PERSON:
            return new Person(name, phone, email, address, tags);
        default:
            throw new ParseException("Unknown role:" + role);
        }
    }

    /**
     * Creates a {@code Person} instance based on the specified {@code Role} and provided details, using
     * fields from the {@code EditPersonDescriptor} and default values from {@code personToEdit} if available.
     *
     * @param role                 The role of the person to create, determining the subclass to instantiate.
     * @param name                 The person's name.
     * @param phone                The person's phone number.
     * @param email                The person's email address.
     * @param address              The person's address.
     * @param tags                 The tags associated with the person.
     * @param editPersonDescriptor The descriptor containing fields to update or add for the new person.
     * @param personToEdit         The original person whose details can be used as defaults for role-specific fields.
     * @return A new instance of the appropriate subclass of {@code Person} based on the specified {@code Role}.
     * @throws CommandException If a required field for the specified role is missing or if the role is unknown.
     */
    public static Person createPerson(Role role, Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                                      EditPersonDescriptor editPersonDescriptor, Person personToEdit)
            throws CommandException {
        switch (role) {
        case VOLUNTEER:
            checkDoesNotExist(role, DONATED_AMOUNT, editPersonDescriptor);
            checkDoesNotExist(role, END_DATE, editPersonDescriptor);
            Hours hours = retrieveVolunteerHours(editPersonDescriptor, personToEdit);
            return new Volunteer(name, phone, email, address, tags, hours);

        case DONOR:
            checkDoesNotExist(role, HOURS, editPersonDescriptor);
            checkDoesNotExist(role, END_DATE, editPersonDescriptor);
            DonatedAmount donatedAmount = retrieveDonorAmount(editPersonDescriptor, personToEdit);
            return new Donor(name, phone, email, address, tags, donatedAmount);

        case PARTNER:
            checkDoesNotExist(role, HOURS, editPersonDescriptor);
            checkDoesNotExist(role, DONATED_AMOUNT, editPersonDescriptor);
            Date endDate = retrievePartnerEndDate(editPersonDescriptor, personToEdit);
            return new Partner(name, phone, email, address, tags, endDate);

        case PERSON:
            checkDoesNotExist(role, HOURS, editPersonDescriptor);
            checkDoesNotExist(role, DONATED_AMOUNT, editPersonDescriptor);
            checkDoesNotExist(role, END_DATE, editPersonDescriptor);
            return new Person(name, phone, email, address, tags);

        default:
            throw new CommandException("Unknown role: " + role);
        }
    }

    private static void checkDoesNotExist(Role role, String field, EditPersonDescriptor editPersonDescriptor)
            throws CommandException {
        switch(field) {
        case "hours":
            if (!editPersonDescriptor.getHours().isEmpty()) {
                throw new CommandException(HOURS_EXIST_ERROR + role);
            }
            break;
        case "donatedAmount":
            if (!editPersonDescriptor.getDonatedAmount().isEmpty()) {
                throw new CommandException(AMOUNT_EXIST_ERROR + role);
            }
            break;
        case "partnershipEndDate":
            if (!editPersonDescriptor.getEndDate().isEmpty()) {
                throw new CommandException(END_DATE_EXIST_ERROR + role);
            }
            break;
        default:
            throw new CommandException("Unknown field: " + field);
        }
    }

    /**
     * Validates and retrieves a required field from {@code ArgumentMultimap} using the given {@code Prefix}.
     * If the field is not present, a {@code ParseException} is thrown with the specified error message.
     *
     * @param argMultimap  The {@code ArgumentMultimap} containing the parsed arguments.
     * @param prefix       The {@code Prefix} used to identify the required field.
     * @param errorMessage The error message to be displayed if the field is missing.
     * @return The string value of the required field.
     * @throws ParseException If the field is missing or not present.
     */
    private static String checkRequiredField(ArgumentMultimap argMultimap, Prefix prefix, String errorMessage)
            throws ParseException {
        if (argMultimap.getValue(prefix).isEmpty()) {
            throw new ParseException(errorMessage);
        }
        return argMultimap.getValue(prefix).get();
    }

    /**
     * Retrieves the hours value for a {@code Volunteer}. If {@code personToEdit} is a {@code Volunteer},
     * its hours value is used as the default. Otherwise, {@code editPersonDescriptor} must contain a non-null hours
     * value.
     *
     * @param descriptor The descriptor containing updated fields.
     * @param person     The original person whose role-specific field may be used as a default.
     * @return The {@code Hours} for the new or updated {@code Volunteer}.
     * @throws CommandException If the hours field is missing and no default is available.
     */
    private static Hours retrieveVolunteerHours(EditPersonDescriptor descriptor, Person person)
            throws CommandException {
        if (person instanceof Volunteer) {
            Volunteer volunteer = (Volunteer) person;
            return descriptor.getHours().orElse(volunteer.getHours());
        } else {
            return descriptor.getHours().orElseThrow(() -> new CommandException(MISSING_HOURS_MESSAGE));
        }
    }

    /**
     * Retrieves the donated amount for a {@code Donor}. If {@code personToEdit} is a {@code Donor},
     * its donated amount is used as the default. Otherwise, {@code editPersonDescriptor} must contain a non-null
     * donated amount.
     *
     * @param descriptor The descriptor containing updated fields.
     * @param person     The original person whose role-specific field may be used as a default.
     * @return The {@code DonatedAmount} for the new or updated {@code Donor}.
     * @throws CommandException If the donated amount field is missing and no default is available.
     */
    private static DonatedAmount retrieveDonorAmount(EditPersonDescriptor descriptor, Person person)
            throws CommandException {
        if (person instanceof Donor) {
            Donor donor = (Donor) person;
            return descriptor.getDonatedAmount().orElse(donor.getDonatedAmount());
        } else {
            return descriptor.getDonatedAmount()
                    .orElseThrow(() -> new CommandException(MISSING_DONATED_AMOUNT_MESSAGE));
        }
    }

    /**
     * Retrieves the partnership end date for a {@code Partner}. If {@code personToEdit} is a {@code Partner},
     * its end date is used as the default. Otherwise, {@code editPersonDescriptor} must contain a non-null end date.
     *
     * @param descriptor The descriptor containing updated fields.
     * @param person     The original person whose role-specific field may be used as a default.
     * @return The {@code Date} for the new or updated {@code Partner}.
     * @throws CommandException If the end date field is missing and no default is available.
     */
    private static Date retrievePartnerEndDate(EditPersonDescriptor descriptor, Person person)
            throws CommandException {
        if (person instanceof Partner) {
            Partner partner = (Partner) person;
            return descriptor.getEndDate().orElse(partner.getEndDate());
        } else {
            return descriptor.getEndDate().orElseThrow(() -> new CommandException(MISSING_END_DATE_MESSAGE));
        }
    }



}

