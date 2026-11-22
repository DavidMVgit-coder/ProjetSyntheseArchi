package player.application.login;

import java.util.UUID;

public record LoginOutput(UUID playerId, String token) {
}
