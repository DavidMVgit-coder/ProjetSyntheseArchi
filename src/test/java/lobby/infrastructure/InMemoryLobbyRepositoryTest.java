package lobby.infrastructure;

import lobby.domain.Lobby;
import lobby.domain.LobbyStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
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
    void findsNoOpenLobbyForAPlayerWhoIsInNone() {
        InMemoryLobbyRepository repository = new InMemoryLobbyRepository();
        repository.create(new Lobby(UUID.randomUUID()));

        assertFalse(repository.existsOpenLobbyWithMember(UUID.randomUUID()));
    }

    @Test
    void findsTheOpenLobbyOfItsHost() {
        InMemoryLobbyRepository repository = new InMemoryLobbyRepository();
        UUID hostId = UUID.randomUUID();
        repository.create(new Lobby(hostId));

        assertTrue(repository.existsOpenLobbyWithMember(hostId));
    }

    @Test
    void findsTheOpenLobbyOfAMemberWhoIsNotTheHost() {
        InMemoryLobbyRepository repository = new InMemoryLobbyRepository();
        UUID hostId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();
        Lobby lobby = mock(Lobby.class);
        when(lobby.getId()).thenReturn(UUID.randomUUID());
        when(lobby.getMemberIds()).thenReturn(List.of(hostId, memberId));
        when(lobby.getStatus()).thenReturn(LobbyStatus.OPEN);
        repository.create(lobby);

        assertTrue(repository.existsOpenLobbyWithMember(memberId));
    }

    @Test
    void ignoresLobbiesThatAreNoLongerOpen() {
        InMemoryLobbyRepository repository = new InMemoryLobbyRepository();
        UUID hostId = UUID.randomUUID();
        Lobby lobby = mock(Lobby.class);
        when(lobby.getId()).thenReturn(UUID.randomUUID());
        when(lobby.getMemberIds()).thenReturn(List.of(hostId));
        when(lobby.getStatus()).thenReturn(LobbyStatus.STARTED);
        repository.create(lobby);

        assertFalse(repository.existsOpenLobbyWithMember(hostId));
    }
}
