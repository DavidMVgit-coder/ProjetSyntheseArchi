package lobby.domain.exception;

import java.util.UUID;

public class PlayerNotInLobbyException extends RuntimeException {

    public PlayerNotInLobbyException(UUID playerId) {
        super("Le joueur '" + playerId + "' n'est pas membre de ce lobby.");
    }
}
