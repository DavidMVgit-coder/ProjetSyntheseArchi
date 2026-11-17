package lobby.infrastructure;

import lobby.application.port.LobbyRepository;
import lobby.domain.Lobby;
import lobby.domain.LobbyStatus;
import lobby.domain.exception.PlayerAlreadyInOpenLobbyException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryLobbyRepository implements LobbyRepository {

    private final Map<UUID, Lobby> lobbies = new HashMap<>();

    @Override
    public synchronized void create(Lobby lobby) {
        UUID playerId = lobby.getHostId();
        for (Lobby existingLobby : lobbies.values()) {
            if (existingLobby.getStatus() == LobbyStatus.OPEN
                    && existingLobby.getMemberIds().contains(playerId)) {
                throw new PlayerAlreadyInOpenLobbyException(playerId);
            }
        }

        lobbies.put(lobby.getId(), lobby);
    }

    @Override
    public synchronized Optional<Lobby> findById(UUID id) {
        return Optional.ofNullable(lobbies.get(id));
    }
}
