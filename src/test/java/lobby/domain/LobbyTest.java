package lobby.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
