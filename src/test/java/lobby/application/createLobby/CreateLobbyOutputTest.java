package lobby.application.createLobby;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateLobbyOutputTest {

    private static final int LOBBY_CAPACITY = 4;
    private static final int INITIAL_AVAILABLE_SLOTS = 3;

    @Test
    void keepsAnImmutableSnapshotOfTheMembers() {
        UUID hostId = UUID.randomUUID();
        List<UUID> members = new ArrayList<>(List.of(hostId));
        CreateLobbyOutput output = new CreateLobbyOutput(
                UUID.randomUUID(), hostId, members, LOBBY_CAPACITY, INITIAL_AVAILABLE_SLOTS, "OPEN");

        members.clear();

        assertEquals(List.of(hostId), output.memberIds());
        assertThrows(UnsupportedOperationException.class,
                () -> output.memberIds().add(UUID.randomUUID()));
    }
}
