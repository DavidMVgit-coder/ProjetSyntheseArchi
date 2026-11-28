package lobby.application.createLobby;

import lobby.application.port.LobbyRepository;
import lobby.domain.Lobby;
import lobby.domain.exception.PlayerAlreadyInOpenLobbyException;

import java.util.Objects;
import java.util.UUID;

public class CreateLobbyUseCase {

    private final LobbyRepository repository;

    public CreateLobbyUseCase(LobbyRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public synchronized CreateLobbyOutput execute(UUID authenticatedPlayerId) {
        Objects.requireNonNull(authenticatedPlayerId, "L'identifiant de l'hôte est obligatoire.");

        if (repository.existsOpenLobbyWithMember(authenticatedPlayerId)) {
            throw new PlayerAlreadyInOpenLobbyException(authenticatedPlayerId);
        }

        Lobby lobby = new Lobby(authenticatedPlayerId);
        repository.create(lobby);

        return new CreateLobbyOutput(
                lobby.getId(),
                lobby.getHostId(),
                lobby.getMemberIds(),
                lobby.getCapacity(),
                lobby.getAvailableSlots(),
                lobby.getStatus().name());
    }
}
