package player.infrastructure;

import org.junit.jupiter.api.Test;
import player.domain.PasswordHash;
import player.domain.RawPassword;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Argon2PasswordHasherTest {

    private final Argon2PasswordHasher hasher = new Argon2PasswordHasher();

    @Test
    void producesAnArgon2idHashWithoutThePassword() {
        PasswordHash hash = hasher.hash(new RawPassword("dragon-2026"));

        assertTrue(hash.value().startsWith("$argon2id$"));
        assertFalse(hash.value().contains("dragon-2026"));
    }

    @Test
    void usesARandomSalt() {
        RawPassword password = new RawPassword("dragon-2026");

        assertNotEquals(hasher.hash(password), hasher.hash(password));
    }

    @Test
    void verifiesWithoutComparingInClearText() {
        PasswordHash hash = hasher.hash(new RawPassword("dragon-2026"));

        assertTrue(hasher.matches(new RawPassword("dragon-2026"), hash));
        assertFalse(hasher.matches(new RawPassword("dragon-2027"), hash));
    }
}
