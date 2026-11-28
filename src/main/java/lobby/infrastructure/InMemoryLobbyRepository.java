package lobby.infrastructure;

import lobby.application.port.LobbyRepository;
import lobby.domain.Lobby;
import lobby.domain.LobbyStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryLobbyRepository implements LobbyRepository {

    private final Map<UUID, Lobby> lobbies = new HashMap<>();

    @Override
    public synchronized void create(Lobby lobby) {
        lobbies.put(lobby.getId(), lobby);
    }

    @Override
    public synchronized Optional<Lobby> findById(UUID id) {
        return Optional.ofNullable(lobbies.get(id));
    }

    @Override
    public synchronized void save(Lobby lobby) {
        lobbies.put(lobby.getId(), lobby);
    }

    @Override
    public synchronized boolean existsOpenLobbyWithMember(UUID playerId) {
        return lobbies.values().stream()
                .anyMatch(lobby -> lobby.getStatus() == LobbyStatus.OPEN
                        && lobby.getMemberIds().contains(playerId));
    }
}
