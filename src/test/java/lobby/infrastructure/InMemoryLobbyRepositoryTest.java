package lobby.infrastructure;

import lobby.domain.Lobby;
import lobby.domain.LobbyStatus;
import lobby.domain.exception.PlayerAlreadyInOpenLobbyException;
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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InMemoryLobbyRepositoryTest {

    @Test
    void returnsEmptyForAnUnknownLobby() {
        InMemoryLobbyRepository repository = new InMemoryLobbyRepository();

        assertTrue(repository.findById(UUID.randomUUID()).isEmpty());
    }

    @Test
    void storesIndependentLobbiesForDifferentPlayers() {
        InMemoryLobbyRepository repository = new InMemoryLobbyRepository();
        Lobby first = new Lobby(UUID.randomUUID());
        Lobby second = new Lobby(UUID.randomUUID());

        repository.create(first);
        repository.create(second);

        assertSame(first, repository.findById(first.getId()).orElseThrow());
        assertSame(second, repository.findById(second.getId()).orElseThrow());
    }

    @Test
    void rejectsASecondLobbyForAnExistingHostWithoutSavingIt() {
        InMemoryLobbyRepository repository = new InMemoryLobbyRepository();
        UUID playerId = UUID.randomUUID();
        Lobby existing = new Lobby(playerId);
        Lobby attempted = new Lobby(playerId);
        repository.create(existing);

        assertThrows(PlayerAlreadyInOpenLobbyException.class, () -> repository.create(attempted));

        assertSame(existing, repository.findById(existing.getId()).orElseThrow());
        assertTrue(repository.findById(attempted.getId()).isEmpty());
    }

    @Test
    void rejectsAPlayerWhoIsAMemberOfAnotherHostsOpenLobby() {
        InMemoryLobbyRepository repository = new InMemoryLobbyRepository();
        UUID hostId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();
        Lobby existing = mock(Lobby.class);
        when(existing.getId()).thenReturn(UUID.randomUUID());
        when(existing.getHostId()).thenReturn(hostId);
        when(existing.getMemberIds()).thenReturn(List.of(hostId, memberId));
        when(existing.getStatus()).thenReturn(LobbyStatus.OPEN);
        repository.create(existing);
        Lobby attempted = new Lobby(memberId);

        assertThrows(PlayerAlreadyInOpenLobbyException.class, () -> repository.create(attempted));

        assertSame(existing, repository.findById(existing.getId()).orElseThrow());
        assertTrue(repository.findById(attempted.getId()).isEmpty());
    }

    @Test
    @Timeout(15)
    void acceptsExactlyOneOfTwoConcurrentCreationsForTheSamePlayer() throws Exception {
        InMemoryLobbyRepository repository = new InMemoryLobbyRepository();
        UUID playerId = UUID.randomUUID();
        Lobby first = new Lobby(playerId);
        Lobby second = new Lobby(playerId);
        CyclicBarrier start = new CyclicBarrier(2);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            Future<Boolean> firstResult = executor.submit(() -> createAfterBarrier(repository, first, start));
            Future<Boolean> secondResult = executor.submit(() -> createAfterBarrier(repository, second, start));

            boolean firstCreated = firstResult.get(5, TimeUnit.SECONDS);
            boolean secondCreated = secondResult.get(5, TimeUnit.SECONDS);

            assertNotEquals(firstCreated, secondCreated);
            assertEquals(firstCreated, repository.findById(first.getId()).isPresent());
            assertEquals(secondCreated, repository.findById(second.getId()).isPresent());
        } finally {
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }
    }

    private static boolean createAfterBarrier(
            InMemoryLobbyRepository repository, Lobby lobby, CyclicBarrier start) throws Exception {
        start.await(5, TimeUnit.SECONDS);
        try {
            repository.create(lobby);
            return true;
        } catch (PlayerAlreadyInOpenLobbyException exception) {
            return false;
        }
    }
}
