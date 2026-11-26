package lobby.domain;

import lobby.domain.exception.PlayerNotInLobbyException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LobbyTest {

    private static final int EXPECTED_LOBBY_CAPACITY = 4;
    private static final int EXPECTED_INITIAL_AVAILABLE_SLOTS = 3;

    @Test
    void creatorBecomesHostAndFirstMember() {
        UUID creatorId = UUID.randomUUID();

        Lobby lobby = new Lobby(creatorId);

        assertEquals(creatorId, lobby.getHostId());
        assertEquals(List.of(creatorId), lobby.getMemberIds());
    }

    @Test
    void startsOpenWithCapacityForFourPlayersAndThreeAvailableSlots() {
        Lobby lobby = new Lobby(UUID.randomUUID());

        assertEquals(LobbyStatus.OPEN, lobby.getStatus());
        assertEquals(EXPECTED_LOBBY_CAPACITY, lobby.getCapacity());
        assertEquals(EXPECTED_INITIAL_AVAILABLE_SLOTS, lobby.getAvailableSlots());
    }

    @Test
    void assignsAnIdentifierToEachLobby() {
        Lobby firstLobby = new Lobby(UUID.randomUUID());
        Lobby secondLobby = new Lobby(UUID.randomUUID());

        assertNotNull(firstLobby.getId());
        assertNotNull(secondLobby.getId());
        assertNotEquals(firstLobby.getId(), secondLobby.getId());
    }

    @Test
    void membersCannotBeChangedThroughTheReturnedList() {
        UUID creatorId = UUID.randomUUID();
        Lobby lobby = new Lobby(creatorId);

        assertThrows(UnsupportedOperationException.class,
                () -> lobby.getMemberIds().add(UUID.randomUUID()));
        assertThrows(UnsupportedOperationException.class,
                () -> lobby.getMemberIds().remove(creatorId));

        assertEquals(List.of(creatorId), lobby.getMemberIds());
        assertEquals(EXPECTED_INITIAL_AVAILABLE_SLOTS, lobby.getAvailableSlots());
    }

    @Test
    void rejectsAMissingHost() {
        assertThrows(NullPointerException.class, () -> new Lobby(null));
    }

    @Test
    void aMemberWhoLeavesFreesTheirSeat() {
        UUID host = UUID.randomUUID();
        UUID bram = UUID.randomUUID();
        Lobby lobby = new Lobby(host);
        lobby.join(bram);

        lobby.leave(bram);

        assertEquals(List.of(host), lobby.getMemberIds());
        assertEquals(EXPECTED_INITIAL_AVAILABLE_SLOTS, lobby.getAvailableSlots());
        assertEquals(LobbyStatus.OPEN, lobby.getStatus());
    }

    @Test
    void whenTheHostLeavesTheEarliestArrivedPlayerBecomesHost() {
        UUID host = UUID.randomUUID();
        UUID secondArrived = UUID.randomUUID();
        UUID thirdArrived = UUID.randomUUID();
        Lobby lobby = new Lobby(host);
        lobby.join(secondArrived);
        lobby.join(thirdArrived);

        lobby.leave(host);

        assertEquals(secondArrived, lobby.getHostId());
        assertEquals(List.of(secondArrived, thirdArrived), lobby.getMemberIds());
    }

    @Test
    void whenANonHostLeavesTheHostStaysTheSame() {
        UUID host = UUID.randomUUID();
        UUID bram = UUID.randomUUID();
        Lobby lobby = new Lobby(host);
        lobby.join(bram);

        lobby.leave(bram);

        assertEquals(host, lobby.getHostId());
    }

    @Test
    void whenTheLastPlayerLeavesTheLobbyIsClosed() {
        UUID host = UUID.randomUUID();
        Lobby lobby = new Lobby(host);

        lobby.leave(host);

        assertEquals(LobbyStatus.CLOSED, lobby.getStatus());
        assertTrue(lobby.getMemberIds().isEmpty());
    }

    @Test
    void aPlayerWhoIsNotAMemberCannotLeave() {
        Lobby lobby = new Lobby(UUID.randomUUID());

        assertThrows(PlayerNotInLobbyException.class, () -> lobby.leave(UUID.randomUUID()));
    }

    @Test
    void aPlayerCannotLeaveTwice() {
        UUID host = UUID.randomUUID();
        UUID bram = UUID.randomUUID();
        Lobby lobby = new Lobby(host);
        lobby.join(bram);
        lobby.leave(bram);

        assertThrows(PlayerNotInLobbyException.class, () -> lobby.leave(bram));
        assertFalse(lobby.getMemberIds().contains(bram));
    }
}
