package player.application.createPlayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.domain.exception.EmailAlreadyTakenException;
import player.domain.exception.InvalidPlayerDataException;
import player.domain.exception.PasswordTooShortException;
import player.domain.exception.PseudonymAlreadyTakenException;
import player.infrastructure.InMemoryPlayerRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreatePlayerUseCaseTest {

    private CreatePlayerUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreatePlayerUseCase(new InMemoryPlayerRepository(), new FakePasswordHasher());
    }

    private CreatePlayerOutput create(String pseudonym, String email, String password) {
        return useCase.execute(new CreatePlayerCommand(pseudonym, email, password));
    }

    @Test
    void createsAnAccount() {
        CreatePlayerOutput output = create("Aria", "aria@mail.com", "dragon-2026");

        assertNotNull(output.id());
        assertEquals("Aria", output.pseudonym());
        assertEquals("aria@mail.com", output.email());
    }

    @Test
    void refusesAPseudonymAlreadyTakenIgnoringCase() {
        create("Aria", "aria@mail.com", "dragon-2026");

        assertThrows(PseudonymAlreadyTakenException.class, () -> create("aria", "autre@mail.com", "dragon-2026"));
    }

    @Test
    void refusesAnEmailAlreadyTakenIgnoringCase() {
        create("Aria", "aria@mail.com", "dragon-2026");

        assertThrows(EmailAlreadyTakenException.class, () -> create("Bram", "ARIA@mail.com", "dragon-2026"));
    }

    @Test
    void refusesAPasswordThatIsTooShort() {
        assertThrows(PasswordTooShortException.class, () -> create("Aria", "aria@mail.com", "court"));
    }

    @Test
    void refusesMissingFields() {
        assertThrows(InvalidPlayerDataException.class, () -> create(null, "aria@mail.com", "dragon-2026"));
        assertThrows(InvalidPlayerDataException.class, () -> create("Aria", null, "dragon-2026"));
        assertThrows(InvalidPlayerDataException.class, () -> create("Aria", "aria@mail.com", null));
        assertThrows(InvalidPlayerDataException.class, () -> useCase.execute(null));
    }

    @Test
    void aRefusedRegistrationDoesNotReserveThePseudonym() {
        assertThrows(PasswordTooShortException.class, () -> create("Aria", "aria@mail.com", "court"));

        assertDoesNotThrow(() -> create("Aria", "aria@mail.com", "dragon-2026"));
    }

    @Test
    void commandNeverShowsThePassword() {
        assertFalse(new CreatePlayerCommand("Aria", "aria@mail.com", "dragon-2026").toString().contains("dragon-2026"));
    }
}
