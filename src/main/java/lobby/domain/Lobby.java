package lobby.domain;

import lobby.domain.exception.GameAlreadyStartedException;
import lobby.domain.exception.PlayerNotInLobbyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class Lobby {

    private static final int CAPACITY = 4;

    private final UUID id;
    private UUID hostId;
    private final List<UUID> memberIds = new ArrayList<>();
    private final Map<UUID, UUID> selectedHeroIds = new HashMap<>();
    private LobbyStatus status;

    public Lobby(UUID hostId) {
        this.hostId = Objects.requireNonNull(hostId, "L'identifiant de l'hôte est obligatoire.");
        this.id = UUID.randomUUID();
        this.memberIds.add(hostId);
        this.status = LobbyStatus.OPEN;
    }

    public void join(UUID playerId) {
        Objects.requireNonNull(playerId, "L'identifiant du joueur est obligatoire.");
        if (status != LobbyStatus.OPEN || memberIds.contains(playerId) || memberIds.size() >= CAPACITY) {
            throw new IllegalStateException("Le joueur ne peut pas rejoindre ce lobby.");
        }
        memberIds.add(playerId);
    }

    public void leave(UUID playerId) {
        if (!memberIds.contains(playerId)) {
            throw new PlayerNotInLobbyException(playerId);
        }

        if (status == LobbyStatus.STARTED) {
            throw new GameAlreadyStartedException();
        }

        memberIds.remove(playerId);
        selectedHeroIds.remove(playerId);

        if (memberIds.isEmpty()) {
            status = LobbyStatus.CLOSED;
            return;
        }

        if (playerId.equals(hostId)) {
            hostId = memberIds.get(0);
        }
    }

    public void selectHero(UUID playerId, UUID heroId) {
        Objects.requireNonNull(heroId, "L'identifiant du héros est obligatoire.");
        if (!memberIds.contains(playerId)) {
            throw new PlayerNotInLobbyException(playerId);
        }

        if (status == LobbyStatus.STARTED) {
            throw new GameAlreadyStartedException();
        }

        selectedHeroIds.put(playerId, heroId);
    }

    public Optional<UUID> getSelectedHeroId(UUID playerId) {
        return Optional.ofNullable(selectedHeroIds.get(playerId));
    }

    public UUID getId() {
        return id;
    }

    public UUID getHostId() {
        return hostId;
    }

    public List<UUID> getMemberIds() {
        return List.copyOf(memberIds);
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
