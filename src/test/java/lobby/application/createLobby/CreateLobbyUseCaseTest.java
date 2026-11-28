package lobby.application.createLobby;

import lobby.application.port.LobbyRepository;
import lobby.domain.Lobby;
import lobby.domain.exception.PlayerAlreadyInOpenLobbyException;
import lobby.infrastructure.InMemoryLobbyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class CreateLobbyUseCaseTest {

    private static final int EXPECTED_LOBBY_CAPACITY = 4;
    private static final int EXPECTED_INITIAL_AVAILABLE_SLOTS = 3;
    private static final int CONCURRENT_CREATION_COUNT = 2;
    private static final int TEST_TIMEOUT_SECONDS = 15;
    private static final int WAIT_TIMEOUT_SECONDS = 5;

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
    void rejectsAPlayerAlreadyInAnOpenLobbyWithoutCreatingAnother() {
        LobbyRepository repository = mock(LobbyRepository.class);
        UUID playerId = UUID.randomUUID();
        when(repository.existsOpenLobbyWithMember(playerId)).thenReturn(true);
        CreateLobbyUseCase useCase = new CreateLobbyUseCase(repository);

        assertThrows(PlayerAlreadyInOpenLobbyException.class, () -> useCase.execute(playerId));

        verify(repository, never()).create(any(Lobby.class));
    }

    @Test
    void allowsANewLobbyOnceThePreviousOneIsClosed() {
        LobbyRepository repository = new InMemoryLobbyRepository();
        CreateLobbyUseCase useCase = new CreateLobbyUseCase(repository);
        UUID playerId = UUID.randomUUID();
        Lobby first = repository.findById(useCase.execute(playerId).id()).orElseThrow();
        first.leave(playerId);
        repository.save(first);

        CreateLobbyOutput second = useCase.execute(playerId);

        assertNotEquals(first.getId(), second.id());
    }

    @Test
    void rejectsAMissingPlayerBeforeUsingTheRepository() {
        LobbyRepository repository = mock(LobbyRepository.class);
        CreateLobbyUseCase useCase = new CreateLobbyUseCase(repository);

        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verifyNoInteractions(repository);
    }

    @Test
    @Timeout(TEST_TIMEOUT_SECONDS)
    void acceptsExactlyOneOfTwoConcurrentCreationsForTheSamePlayer() throws Exception {
        CreateLobbyUseCase useCase = new CreateLobbyUseCase(new InMemoryLobbyRepository());
        UUID playerId = UUID.randomUUID();
        CyclicBarrier start = new CyclicBarrier(CONCURRENT_CREATION_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_CREATION_COUNT);

        try {
            Future<Boolean> firstResult = executor.submit(() -> createAfterBarrier(useCase, playerId, start));
            Future<Boolean> secondResult = executor.submit(() -> createAfterBarrier(useCase, playerId, start));

            boolean firstCreated = firstResult.get(WAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            boolean secondCreated = secondResult.get(WAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            assertNotEquals(firstCreated, secondCreated);
        } finally {
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(WAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS));
        }
    }

    private static boolean createAfterBarrier(
            CreateLobbyUseCase useCase, UUID playerId, CyclicBarrier start) throws Exception {
        start.await(WAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        try {
            useCase.execute(playerId);
            return true;
        } catch (PlayerAlreadyInOpenLobbyException exception) {
            return false;
        }
    }
}
