package lobby.application.createLobby;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateLobbyOutputTest {

    @Test
    void keepsAnImmutableSnapshotOfTheMembers() {
        UUID hostId = UUID.randomUUID();
        List<UUID> members = new ArrayList<>(List.of(hostId));
        CreateLobbyOutput output = new CreateLobbyOutput(
                UUID.randomUUID(), hostId, members, 4, 3, "OPEN");

        members.clear();

        assertEquals(List.of(hostId), output.memberIds());
        assertThrows(UnsupportedOperationException.class,
                () -> output.memberIds().add(UUID.randomUUID()));
    }
}
