package lobby.application.createLobby;

import lobby.application.port.LobbyRepository;
import lobby.domain.Lobby;

import java.util.Objects;
import java.util.UUID;

public class CreateLobbyUseCase {

    private final LobbyRepository repository;

    public CreateLobbyUseCase(LobbyRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public CreateLobbyOutput execute(UUID authenticatedPlayerId) {
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
