
package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lobby.application.port.SessionRepository
import domain.exception.SessionFullException;
import domain.exception.PlayerAlreadyInSessionException;
import domain.exception.PlayerNotInSessionException;

public class Session implements SessionRepository{

    private final UUID sessionId;
    private final UUID creatorId;
    private final List<UUID> playerIds;

    private static final int MAX_PLAYERS = 4;

    public Session(UUID sessionId, UUID creatorId) {

        this.sessionId = sessionId;
        this.creatorId = creatorId;

        this.playerIds = new ArrayList<>();

        this.playerIds.add(creatorId);
    }
    @verride
    public void addPlayer(UUID playerId) {

        if (playerIds.contains(playerId)) {
            throw new PlayerAlreadyInSessionException();
        }

        if (playerIds.size() >= MAX_PLAYERS) {
            throw new SessionFullException();
        }

        playerIds.add(playerId);
    }

    @verride
    public void removePlayer(UUID playerId) {

        if (!playerIds.contains(playerId)) {
            throw new PlayerNotInSessionException();
        }

        playerIds.remove(playerId);
    }


    public boolean isFull() {
        return playerIds.size() >= MAX_PLAYERS;
    }


    public boolean containsPlayer(UUID playerId) {
        return playerIds.contains(playerId);
    }


    public int getPlayerCount() {
        return playerIds.size();
    }


    public UUID getSessionId() {
        return sessionId;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public List<UUID> getPlayerIds() {
        return List.copyOf(playerIds);
    }
}
