package player.application.createPlayer;

import java.util.UUID;

public record CreatePlayerOutput(UUID id, String pseudonym, String email) {
}
