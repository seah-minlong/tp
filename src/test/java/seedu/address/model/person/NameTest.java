package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid name
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName("^")); // only non-alphanumeric characters
        assertFalse(Name.isValidName("peter*")); // contains non-alphanumeric characters

        // valid name
        assertTrue(Name.isValidName("peter jack")); // alphabets only
        assertTrue(Name.isValidName("12345")); // numbers only
        assertTrue(Name.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(Name.isValidName("Capital Tan")); // with capital letters
        assertTrue(Name.isValidName("David Roger Jackson Ray Jr 2nd")); // long names
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));
    }

    @Test
    public void compareTo() {
        Name nameAlice = new Name("Alice");
        Name nameBob = new Name("Bob");
        Name nameAliceUpper = new Name("ALICE");
        Name nameAliceLower = new Name("alice");

        // same names (case-sensitive) -> returns 0
        assertTrue(nameAlice.compareTo(new Name("Alice")) == 0);

        // different names -> returns negative if less than, positive if greater than
        assertTrue(nameAlice.compareTo(nameBob) < 0);
        assertTrue(nameBob.compareTo(nameAlice) > 0);

        // names that differ in case -> compareTo is case-sensitive
        assertTrue(nameAlice.compareTo(nameAliceUpper) != 0);
        assertTrue(nameAlice.compareTo(nameAliceLower) != 0);

        // ordering based on ASCII values
        assertTrue(nameAliceUpper.compareTo(nameAliceLower) < 0);
    }

    @Test
    public void compareTo_null_throwsNullPointerException() {
        Name name = new Name("Alice");
        assertThrows(NullPointerException.class, () -> name.compareTo(null));
    }

    @Test
    public void compareToIgnoreCase() {
        Name nameAlice = new Name("Alice");
        Name nameBob = new Name("Bob");
        Name nameAliceUpper = new Name("ALICE");
        Name nameAliceLower = new Name("alice");

        // same names regardless of case -> returns 0
        assertTrue(nameAlice.compareToIgnoreCase(nameAliceUpper) == 0);
        assertTrue(nameAlice.compareToIgnoreCase(nameAliceLower) == 0);

        // different names -> returns negative if less than, positive if greater than
        assertTrue(nameAlice.compareToIgnoreCase(nameBob) < 0);
        assertTrue(nameBob.compareToIgnoreCase(nameAlice) > 0);

        // comparing names with different cases
        assertTrue(nameAliceUpper.compareToIgnoreCase(nameAliceLower) == 0);
    }

    @Test
    public void compareToIgnoreCase_null_throwsNullPointerException() {
        Name name = new Name("Alice");
        assertThrows(NullPointerException.class, () -> name.compareToIgnoreCase(null));
    }
}
