package api.lobby;

import lobby.application.createLobby.CreateLobbyOutput;

import java.util.List;
import java.util.UUID;

public record LobbyResponse(
        UUID id,
        UUID hostId,
        List<UUID> memberIds,
        int capacity,
        int availableSlots,
        String status) {

    public static LobbyResponse from(CreateLobbyOutput output) {
        return new LobbyResponse(output.id(), output.hostId(), output.memberIds(),
                output.capacity(), output.availableSlots(), output.status());
    }
}
