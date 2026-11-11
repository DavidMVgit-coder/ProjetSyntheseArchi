package lobby.domain.exception;

import java.util.UUID;

public class PlayerAlreadyInOpenLobbyException extends RuntimeException {

    public PlayerAlreadyInOpenLobbyException(UUID playerId) {
        super("Le joueur '" + playerId + "' est déjà membre d'un lobby ouvert.");
    }
}
