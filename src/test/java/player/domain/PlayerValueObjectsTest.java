package player.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import player.domain.exception.InvalidPlayerDataException;
import player.domain.exception.PasswordTooShortException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerValueObjectsTest {

    @Test
    void acceptsAValidPseudonymAndKeepsItsCase() {
        assertEquals("Aria_42", new Pseudonym("Aria_42").getValue());
    }

    @Test
    void pseudonymsAreEqualIgnoringCase() {
        assertEquals(new Pseudonym("Aria"), new Pseudonym("aria"));
        assertEquals(new Pseudonym("Aria").hashCode(), new Pseudonym("aria").hashCode());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"ab", "aaaaaaaaaaaaaaaaaaaaa", "Ar ia", "Aria!", "a-b-c"})
    void rejectsInvalidPseudonyms(String value) {
        assertThrows(InvalidPlayerDataException.class, () -> new Pseudonym(value));
    }

    @Test
    void acceptsPseudonymLengthLimits() {
        assertDoesNotThrow(() -> new Pseudonym("abc"));
        assertDoesNotThrow(() -> new Pseudonym("a".repeat(20)));
    }

    @Test
    void emailsAreEqualIgnoringCase() {
        assertEquals(new Email("Aria@Mail.com"), new Email("aria@mail.com"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"aria", "aria@", "@mail.com", "aria@mail", "a b@mail.com", "a@@mail.com"})
    void rejectsInvalidEmails(String value) {
        assertThrows(InvalidPlayerDataException.class, () -> new Email(value));
    }

    @Test
    void missingPasswordIsAnInvalidRequest() {
        assertThrows(InvalidPlayerDataException.class, () -> new RawPassword(null));
    }

    @Test
    void rejectsPasswordsShorterThanEightCharacters() {
        assertThrows(PasswordTooShortException.class, () -> new RawPassword("1234567"));
        assertDoesNotThrow(() -> new RawPassword("12345678"));
    }

    @Test
    void neverPrintsPasswordsOrHashes() {
        assertFalse(new RawPassword("dragon-2026").toString().contains("dragon"));
        assertFalse(new PasswordHash("$argon2id$secret").toString().contains("secret"));
    }
}
