package lobby.application.createLobby;

import lobby.application.port.LobbyRepository;
import lobby.domain.Lobby;
import lobby.domain.exception.PlayerAlreadyInOpenLobbyException;
import lobby.infrastructure.InMemoryLobbyRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class CreateLobbyUseCaseTest {

    private static final int EXPECTED_LOBBY_CAPACITY = 4;
    private static final int EXPECTED_INITIAL_AVAILABLE_SLOTS = 3;

    @Test
    void createsAndStoresAnOpenLobbyForItsFirstMemberAndHost() {
        LobbyRepository repository = new InMemoryLobbyRepository();
        CreateLobbyUseCase useCase = new CreateLobbyUseCase(repository);
        UUID playerId = UUID.randomUUID();

        CreateLobbyOutput output = useCase.execute(playerId);

        Lobby storedLobby = repository.findById(output.id()).orElseThrow();
        assertEquals(playerId, output.hostId());
        assertEquals(List.of(playerId), output.memberIds());
        assertEquals(EXPECTED_LOBBY_CAPACITY, output.capacity());
        assertEquals(EXPECTED_INITIAL_AVAILABLE_SLOTS, output.availableSlots());
        assertEquals("OPEN", output.status());
        assertEquals(output.hostId(), storedLobby.getHostId());
        assertEquals(output.memberIds(), storedLobby.getMemberIds());
    }

    @Test
    void propagatesTheConflictWithoutRetryingPersistence() {
        LobbyRepository repository = mock(LobbyRepository.class);
        UUID playerId = UUID.randomUUID();
        PlayerAlreadyInOpenLobbyException conflict = new PlayerAlreadyInOpenLobbyException(playerId);
        doThrow(conflict).when(repository).create(any(Lobby.class));
        CreateLobbyUseCase useCase = new CreateLobbyUseCase(repository);

        assertSame(conflict, assertThrows(PlayerAlreadyInOpenLobbyException.class,
                () -> useCase.execute(playerId)));

        verify(repository).create(any(Lobby.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void rejectsAMissingPlayerBeforeUsingTheRepository() {
        LobbyRepository repository = mock(LobbyRepository.class);
        CreateLobbyUseCase useCase = new CreateLobbyUseCase(repository);

        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verifyNoInteractions(repository);
    }
}
