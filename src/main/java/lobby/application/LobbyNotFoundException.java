package lobby.application;

import java.util.UUID;

public class LobbyNotFoundException extends RuntimeException {

    public LobbyNotFoundException(UUID lobbyId) {
        super("Le lobby '" + lobbyId + "' est introuvable.");
    }
}
