package lobby.application.selectHero;

import lobby.application.ForbiddenActionException;
import lobby.application.LobbyNotFoundException;
import lobby.application.port.HeroSummary;
import lobby.domain.Lobby;
import lobby.domain.exception.PlayerNotInLobbyException;
import lobby.infrastructure.InMemoryActiveGames;
import lobby.infrastructure.InMemoryHeroCatalog;
import lobby.infrastructure.InMemoryLobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SelectHeroUseCaseTest {

    private final UUID aria = UUID.randomUUID();
    private final UUID bram = UUID.randomUUID();
    private final UUID thorinId = UUID.randomUUID();
    private final UUID eldrinId = UUID.randomUUID();

    private InMemoryLobbyRepository lobbyRepository;
    private InMemoryHeroCatalog heroCatalog;
    private InMemoryActiveGames activeGames;
    private SelectHeroUseCase useCase;
    private Lobby lobby;

    @BeforeEach
    void setUp() {
        lobbyRepository = new InMemoryLobbyRepository();
        heroCatalog = new InMemoryHeroCatalog();
        activeGames = new InMemoryActiveGames();
        useCase = new SelectHeroUseCase(lobbyRepository, heroCatalog, activeGames);

        heroCatalog.add(new HeroSummary(thorinId, "Thorin", aria));
        heroCatalog.add(new HeroSummary(eldrinId, "Eldrin", bram));

        lobby = new Lobby(aria);
        lobby.join(bram);
        lobbyRepository.create(lobby);
    }

    @Test
    void aMemberSelectsOneOfTheirHeroes() {
        SelectHeroOutput output = useCase.execute(lobby.getId(), aria, thorinId, aria);

        assertEquals(Optional.of(thorinId), lobby.getSelectedHeroId(aria));
        assertEquals(thorinId, output.players().get(0).heroId());
        assertEquals(null, output.players().get(1).heroId());
    }

    @Test
    void aPlayerCannotSelectForAnotherPlayer() {
        assertThrows(ForbiddenActionException.class,
                () -> useCase.execute(lobby.getId(), bram, eldrinId, aria));
    }

    @Test
    void aPlayerCannotSelectTheHeroOfAnotherPlayer() {
        assertThrows(ForbiddenActionException.class,
                () -> useCase.execute(lobby.getId(), aria, eldrinId, aria));
    }

    @Test
    void anUnknownHeroIsRefused() {
        assertThrows(UnknownHeroException.class,
                () -> useCase.execute(lobby.getId(), aria, UUID.randomUUID(), aria));
    }

    @Test
    void aHeroAlreadyInAGameIsRefused() {
        activeGames.add(thorinId);

        assertThrows(HeroInGameException.class,
                () -> useCase.execute(lobby.getId(), aria, thorinId, aria));
    }

    @Test
    void anUnknownLobbyIsRefused() {
        assertThrows(LobbyNotFoundException.class,
                () -> useCase.execute(UUID.randomUUID(), aria, thorinId, aria));
    }

    @Test
    void aPlayerWhoIsNotAMemberIsRefused() {
        UUID carla = UUID.randomUUID();
        UUID carlaHero = UUID.randomUUID();
        heroCatalog.add(new HeroSummary(carlaHero, "Carla", carla));

        assertThrows(PlayerNotInLobbyException.class,
                () -> useCase.execute(lobby.getId(), carla, carlaHero, carla));
    }
}
