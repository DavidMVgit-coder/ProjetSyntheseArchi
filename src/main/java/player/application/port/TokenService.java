package player.application.port;

import java.util.Optional;
import java.util.UUID;

public interface TokenService {

    String issue(UUID playerId);
    Optional<UUID> verify(String token);
}
