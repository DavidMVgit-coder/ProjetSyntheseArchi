package lobby.domain;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Lobby {

    private static final int CAPACITY = 4;

    private final UUID id;
    private final UUID hostId;
    private final List<UUID> memberIds;
    private final LobbyStatus status;

    public Lobby(UUID hostId) {
        this.hostId = Objects.requireNonNull(hostId, "L'identifiant de l'hôte est obligatoire.");
        this.id = UUID.randomUUID();
        this.memberIds = List.of(hostId);
        this.status = LobbyStatus.OPEN;
    }

    public UUID getId() {
        return id;
    }

    public UUID getHostId() {
        return hostId;
    }

    public List<UUID> getMemberIds() {
        return memberIds;
    }

    public LobbyStatus getStatus() {
        return status;
    }

    public int getCapacity() {
        return CAPACITY;
    }

    public int getAvailableSlots() {
        return CAPACITY - memberIds.size();
    }
}
