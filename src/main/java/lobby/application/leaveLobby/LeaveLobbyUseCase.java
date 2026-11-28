package lobby.application.leaveLobby;

import lobby.application.ForbiddenActionException;
import lobby.application.LobbyNotFoundException;
import lobby.application.port.LobbyRepository;
import lobby.domain.Lobby;

import java.util.Objects;
import java.util.UUID;

public class LeaveLobbyUseCase {

    private final LobbyRepository repository;

    public LeaveLobbyUseCase(LobbyRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public void execute(UUID lobbyId, UUID playerToRemoveId, UUID authenticatedPlayerId) {
        Lobby lobby = repository.findById(lobbyId)
                .orElseThrow(() -> new LobbyNotFoundException(lobbyId));

        if (!playerToRemoveId.equals(authenticatedPlayerId)) {
            throw new ForbiddenActionException("Un joueur ne peut retirer que lui-même d'un lobby.");
        }

        lobby.leave(playerToRemoveId);

        repository.save(lobby);
    }
}
