package lobby.application.port;

import lobby.domain.Lobby;

import java.util.Optional;
import java.util.UUID;

public interface LobbyRepository {

    void create(Lobby lobby);

    Optional<Lobby> findById(UUID id);

    void save(Lobby lobby);
}
