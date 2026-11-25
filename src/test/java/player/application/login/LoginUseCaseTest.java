package player.application.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.application.createPlayer.FakePasswordHasher;
import player.application.port.PasswordHasher;
import player.application.port.TokenService;
import player.domain.Email;
import player.domain.Player;
import player.domain.Pseudonym;
import player.domain.RawPassword;
import player.infrastructure.InMemoryPlayerRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginUseCaseTest {

    private final PasswordHasher hasher = new FakePasswordHasher();
    private InMemoryPlayerRepository players;
    private LoginUseCase useCase;
    private Player aria;

    @BeforeEach
    void setUp() {
        players = new InMemoryPlayerRepository();
        aria = new Player(new Pseudonym("Aria"), new Email("aria@mail.com"), hasher.hash(new RawPassword("dragon-2026")));
        players.create(aria);
        useCase = new LoginUseCase(players, hasher, new FakeTokenService());
    }

    @Test
    void returnsATokenForValidCredentials() {
        LoginOutput output = useCase.execute(new LoginCommand("ARIA@mail.com", "dragon-2026"));

        assertEquals(aria.getId(), output.playerId());
        assertEquals("token-" + aria.getId(), output.token());
    }

    @Test
    void refusesAWrongPassword() {
        assertThrows(InvalidCredentialsException.class,
                () -> useCase.execute(new LoginCommand("aria@mail.com", "mauvais-mdp")));
    }

    @Test
    void refusesAnUnknownOrMalformedEmailWithTheSameError() {
        assertThrows(InvalidCredentialsException.class,
                () -> useCase.execute(new LoginCommand("inconnu@mail.com", "dragon-2026")));
        assertThrows(InvalidCredentialsException.class,
                () -> useCase.execute(new LoginCommand("pas-un-courriel", "dragon-2026")));
    }

    @Test
    void refusesATooShortPasswordAsInvalidCredentials() {
        assertThrows(InvalidCredentialsException.class,
                () -> useCase.execute(new LoginCommand("aria@mail.com", "court")));
    }

    @Test
    void refusesMissingFields() {
        assertThrows(InvalidLoginInputException.class, () -> useCase.execute(null));
        assertThrows(InvalidLoginInputException.class, () -> useCase.execute(new LoginCommand(" ", "dragon-2026")));
        assertThrows(InvalidLoginInputException.class, () -> useCase.execute(new LoginCommand("aria@mail.com", "")));
    }

    private static final class FakeTokenService implements TokenService {

        @Override
        public String issue(UUID playerId) {
            return "token-" + playerId;
        }

        @Override
        public Optional<UUID> verify(String token) {
            return Optional.empty();
        }
    }
}
