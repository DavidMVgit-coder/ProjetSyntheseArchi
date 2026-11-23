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
        // 1. Le lobby doit exister.
        Lobby lobby = repository.findById(lobbyId)
                .orElseThrow(() -> new LobbyNotFoundException(lobbyId));

        // 2. Un joueur ne peut retirer que lui-même.
        if (!playerToRemoveId.equals(authenticatedPlayerId)) {
            throw new ForbiddenActionException("Un joueur ne peut retirer que lui-même d'un lobby.");
        }

        // 3. Les règles du lobby (membre, partie lancée, hôte, fermeture) sont dans le domaine.
        lobby.leave(playerToRemoveId);

        // 4. On enregistre le lobby modifié.
        repository.save(lobby);
    }
}
