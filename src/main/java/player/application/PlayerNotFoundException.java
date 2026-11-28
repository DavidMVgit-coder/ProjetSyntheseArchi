package player.application;

import java.util.UUID;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(UUID playerId) {
        super("Le joueur '" + playerId + "' est introuvable.");
    }
}
