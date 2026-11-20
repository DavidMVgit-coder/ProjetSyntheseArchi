package lobby.application.leaveLobby;

import lobby.application.ForbiddenActionException;
import lobby.application.LobbyNotFoundException;
import lobby.domain.Lobby;
import lobby.domain.LobbyStatus;
import lobby.infrastructure.InMemoryLobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LeaveLobbyUseCaseTest {

    private final UUID host = UUID.randomUUID();
    private final UUID bram = UUID.randomUUID();

    private InMemoryLobbyRepository repository;
    private LeaveLobbyUseCase useCase;
    private Lobby lobby;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLobbyRepository();
        useCase = new LeaveLobbyUseCase(repository);

        lobby = new Lobby(host);
        lobby.join(bram);
        repository.create(lobby);
    }

    @Test
    void aPlayerCanLeaveALobby() {
        useCase.execute(lobby.getId(), bram, bram);

        Lobby saved = repository.findById(lobby.getId()).orElseThrow();
        assertEquals(List.of(host), saved.getMemberIds());
    }

    @Test
    void theLastPlayerLeavingClosesTheLobby() {
        useCase.execute(lobby.getId(), bram, bram);
        useCase.execute(lobby.getId(), host, host);

        Lobby saved = repository.findById(lobby.getId()).orElseThrow();
        assertEquals(LobbyStatus.CLOSED, saved.getStatus());
    }

    @Test
    void aPlayerCannotRemoveAnotherPlayer() {
        assertThrows(ForbiddenActionException.class,
                () -> useCase.execute(lobby.getId(), bram, host));

        assertEquals(List.of(host, bram), lobby.getMemberIds());
    }

    @Test
    void anUnknownLobbyIsRefused() {
        UUID unknownLobbyId = UUID.randomUUID();

        assertThrows(LobbyNotFoundException.class,
                () -> useCase.execute(unknownLobbyId, bram, bram));
    }
}
