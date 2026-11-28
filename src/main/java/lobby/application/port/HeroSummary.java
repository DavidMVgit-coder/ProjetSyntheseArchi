package lobby.application.port;

import java.util.UUID;

public record HeroSummary(UUID id, String name, UUID ownerId) {
}
