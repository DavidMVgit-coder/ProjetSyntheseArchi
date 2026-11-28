package player.application.getPlayerProfile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.application.PlayerNotFoundException;
import player.domain.Email;
import player.domain.PasswordHash;
import player.domain.Player;
import player.domain.PlayerFactory;
import player.domain.Pseudonym;
import player.infrastructure.InMemoryPlayerRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetPlayerProfileUseCaseTest {

    private static final String HASH = "$argon2id$secret-hash";

    private InMemoryPlayerRepository repository;
    private GetPlayerProfileUseCase useCase;
    private Player aria;
    private Player bram;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPlayerRepository();
        useCase = new GetPlayerProfileUseCase(repository);
        aria = player("Aria", "aria@mail.com");
        bram = player("Bram", "bram@mail.com");
        repository.create(aria);
        repository.create(bram);
    }

    private Player player(String pseudonym, String email) {
        return PlayerFactory.create(new Pseudonym(pseudonym), new Email(email), new PasswordHash(HASH));
    }

    @Test
    void aPlayerSeesTheirIdPseudonymAndEmail() {
        PlayerProfileOutput profile = useCase.execute(aria.getId().toString(), aria.getId());

        assertEquals(aria.getId(), profile.id());
        assertEquals("Aria", profile.pseudonym());
        assertEquals("aria@mail.com", profile.email());
    }

    @Test
    void theProfileNeverContainsThePasswordHash() {
        PlayerProfileOutput profile = useCase.execute(aria.getId().toString(), aria.getId());

        assertFalse(profile.toString().contains("secret-hash"));
        assertEquals(3, PlayerProfileOutput.class.getRecordComponents().length);
    }

    @Test
    void aPlayerCannotSeeAnotherPlayersProfile() {
        assertThrows(ProfileAccessForbiddenException.class,
                () -> useCase.execute(bram.getId().toString(), aria.getId()));
    }

    @Test
    void anotherIdIsForbiddenEvenIfNoPlayerHasIt() {
        assertThrows(ProfileAccessForbiddenException.class,
                () -> useCase.execute(UUID.randomUUID().toString(), aria.getId()));
        assertThrows(ProfileAccessForbiddenException.class,
                () -> useCase.execute("pas-un-identifiant", aria.getId()));
        assertThrows(ProfileAccessForbiddenException.class,
                () -> useCase.execute(null, aria.getId()));
    }

    @Test
    void anAuthenticatedPlayerWithoutAccountGetsPlayerNotFound() {
        UUID unknownPlayer = UUID.randomUUID();

        assertThrows(PlayerNotFoundException.class,
                () -> useCase.execute(unknownPlayer.toString(), unknownPlayer));
    }

    @Test
    void theIdComparisonIgnoresCase() {
        PlayerProfileOutput profile = useCase.execute(aria.getId().toString().toUpperCase(), aria.getId());

        assertEquals(aria.getId(), profile.id());
    }
}
