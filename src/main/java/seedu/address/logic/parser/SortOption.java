package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.model.person.Person;
import seedu.address.model.person.comparators.NameComparator;
import seedu.address.model.person.comparators.VolunteerComparator;

/**
 * Contains valid sorting options for the sort command.
 */
public enum SortOption {
    NAME("name") {
        @Override
        public Comparator<Person> getComparator() {
            return new NameComparator();
        }
    },
    HOURS("hours") {
        @Override
        public Comparator<Person> getComparator() {
            return new VolunteerComparator();
        }
    };
    // Add more sorting options here if needed

    public static final String MESSAGE_CONSTRAINTS = "Invalid sort option.\nValid options are: "
            + getValidSortOptionsAsString();

    public static final String MESSAGE_EMPTY_SORT_OPTION = "Sort option cannot be empty.";

    private final String value;

    /**
     * Constructs a {@code SortOption}.
     *
     * @param value A valid sort option.
     */
    SortOption(String value) {
        this.value = value;
    }

    /**
     * Parses a {@code String sortOption} into a {@code SortOption}.
     *
     * @param sortOption The sort option string to parse.
     * @return The corresponding {@code SortOption}.
     * @throws IllegalArgumentException If the sort option is invalid.
     */
    public static SortOption fromString(String sortOption) {
        requireNonNull(sortOption);
        sortOption = sortOption.toLowerCase();
        for (SortOption option : SortOption.values()) {
            if (option.value.equalsIgnoreCase(sortOption)) {
                return option;
            }
        }
        throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns the appropriate comparator for the sort option.
     *
     * @return {@code Comparator<Person>} based on the sort option.
     */
    public abstract Comparator<Person> getComparator();

    /**
     * Returns an unmodifiable list of valid sort option strings.
     *
     * @return List of valid sort options.
     */
    private static List<String> getValidSortOptions() {
        return Collections.unmodifiableList(
                Arrays.stream(SortOption.values())
                        .map(SortOption::toString)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Returns a comma-separated string of valid sort options.
     *
     * @return String of valid sort options.
     */
    private static String getValidSortOptionsAsString() {
        return String.join(", ", getValidSortOptions());
    }

    @Override
    public String toString() {
        return value;
    }
}
