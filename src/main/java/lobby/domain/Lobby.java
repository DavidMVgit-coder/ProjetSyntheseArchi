package lobby.domain;

import lobby.domain.exception.GameAlreadyStartedException;
import lobby.domain.exception.PlayerNotInLobbyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Lobby {

    private static final int CAPACITY = 4;

    private final UUID id;
    private UUID hostId;
    // Liste dans l'ordre d'arrivée : le premier de la liste est arrivé le plus tôt.
    private final List<UUID> memberIds = new ArrayList<>();
    private LobbyStatus status;

    public Lobby(UUID hostId) {
        this.hostId = Objects.requireNonNull(hostId, "L'identifiant de l'hôte est obligatoire.");
        this.id = UUID.randomUUID();
        this.memberIds.add(hostId);
        this.status = LobbyStatus.OPEN;
    }

    /**
     * Version minimale pour pouvoir avoir plusieurs membres.
     * Les vraies règles et erreurs de L3 (LOBBY_FULL, ALREADY_MEMBER, ...) seront ajoutées avec la story L3.
     */
    public void join(UUID playerId) {
        Objects.requireNonNull(playerId, "L'identifiant du joueur est obligatoire.");
        if (status != LobbyStatus.OPEN || memberIds.contains(playerId) || memberIds.size() >= CAPACITY) {
            throw new IllegalStateException("Le joueur ne peut pas rejoindre ce lobby.");
        }
        memberIds.add(playerId);
    }

    
    public void leave(UUID playerId) {
        // 1. Le joueur doit être membre du lobby.
        if (!memberIds.contains(playerId)) {
            throw new PlayerNotInLobbyException(playerId);
        }

        // 2. On ne peut plus quitter une fois la partie lancée.
        if (status == LobbyStatus.STARTED) {
            throw new GameAlreadyStartedException();
        }

        // 3. Le joueur libère sa place.
        memberIds.remove(playerId);

        // 4. Plus personne : le lobby est fermé.
        if (memberIds.isEmpty()) {
            status = LobbyStatus.CLOSED;
            return;
        }

        // 5. L'hôte est parti : le joueur arrivé le plus tôt devient l'hôte.
        if (playerId.equals(hostId)) {
            hostId = memberIds.get(0);
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getHostId() {
        return hostId;
    }

    public List<UUID> getMemberIds() {
        // Copie non modifiable : personne ne peut changer les membres sans passer par join/leave.
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
