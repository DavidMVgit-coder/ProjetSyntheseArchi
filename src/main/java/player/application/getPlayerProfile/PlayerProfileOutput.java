package player.application.getPlayerProfile;

import java.util.UUID;

public record PlayerProfileOutput(UUID id, String pseudonym, String email) {
}
