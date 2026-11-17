package lobby.application.createLobby;

import java.util.List;
import java.util.UUID;

public record CreateLobbyOutput(
        UUID id,
        UUID hostId,
        List<UUID> memberIds,
        int capacity,
        int availableSlots,
        String status) {

    public CreateLobbyOutput {
        memberIds = List.copyOf(memberIds);
    }
}
