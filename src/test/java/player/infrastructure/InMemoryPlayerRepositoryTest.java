package player.infrastructure;

import org.junit.jupiter.api.Test;
import player.domain.Email;
import player.domain.PasswordHash;
import player.domain.Player;
import player.domain.PlayerFactory;
import player.domain.Pseudonym;
import player.domain.exception.EmailAlreadyTakenException;
import player.domain.exception.PseudonymAlreadyTakenException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryPlayerRepositoryTest {

    private final InMemoryPlayerRepository repository = new InMemoryPlayerRepository();

    private Player player(String pseudonym, String email) {
        return PlayerFactory.create(new Pseudonym(pseudonym), new Email(email), new PasswordHash("hash"));
    }

    @Test
    void rejectsADuplicatePseudonymIgnoringCase() {
        repository.create(player("Aria", "aria@mail.com"));

        assertThrows(PseudonymAlreadyTakenException.class, () -> repository.create(player("aria", "x@mail.com")));
    }

    @Test
    void rejectsADuplicateEmailIgnoringCase() {
        repository.create(player("Aria", "aria@mail.com"));

        assertThrows(EmailAlreadyTakenException.class, () -> repository.create(player("Bram", "ARIA@mail.com")));
    }

    @Test
    void reportsThePseudonymFirstWhenBothAreTakenByDifferentPlayers() {
        repository.create(player("Aria", "aria@mail.com"));
        repository.create(player("Bram", "bram@mail.com"));

        assertThrows(PseudonymAlreadyTakenException.class,
                () -> repository.create(player("Bram", "aria@mail.com")));
    }

    @Test
    void acceptsDifferentPlayers() {
        repository.create(player("Aria", "aria@mail.com"));

        assertDoesNotThrow(() -> repository.create(player("Bram", "bram@mail.com")));
    }

    @Test
    void findsAPlayerByItsId() {
        Player aria = player("Aria", "aria@mail.com");
        repository.create(aria);

        assertEquals(aria, repository.findById(aria.getId()).orElseThrow());
    }

    @Test
    void returnsEmptyForAnUnknownId() {
        assertTrue(repository.findById(UUID.randomUUID()).isEmpty());
    }
}
